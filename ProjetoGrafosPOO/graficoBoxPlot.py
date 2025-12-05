#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import sys
import re
import pandas as pd
import matplotlib.pyplot as plt

# === Diretórios ===
BASE_DIR = os.path.join("out")  # onde estão os CSVs gerados pelo Java
PLOTS_DIR = "plots"                           # para salvar as imagens
os.makedirs(PLOTS_DIR, exist_ok=True)

# ============================
# Utilidades de listagem/escolha
# ============================
def listar_csvs():
    if not os.path.isdir(BASE_DIR):
        print(f"[ERRO] Diretório não encontrado: {BASE_DIR}")
        sys.exit(1)
    arquivos = [f for f in os.listdir(BASE_DIR) if f.lower().endswith(".csv")]
    if not arquivos:
        print(f"[ERRO] Nenhum CSV encontrado em {BASE_DIR}/")
        sys.exit(1)
    return sorted(arquivos)

def escolher_arquivo(msg):
    arquivos = listar_csvs()
    print("\nArquivos disponíveis:")
    for i, nome in enumerate(arquivos, 1):
        print(f"{i}) {nome}")
    esc = input(msg).strip()
    try:
        idx = int(esc) - 1
        return arquivos[idx]
    except:
        print("[ERRO] Seleção inválida.")
        sys.exit(1)

# ============================
# Normalizações e detecção de colunas
# ============================
def _guess_col(df, candidates, required=True):
    cols_lower = {c.lower(): c for c in df.columns}
    for cand in candidates:
        if cand.lower() in cols_lower:
            return cols_lower[cand.lower()]
    if required:
        raise ValueError(f"Colunas esperadas: {candidates}, encontradas: {list(df.columns)}")
    return None

def _normalize_rep(value):
    if not isinstance(value, str):
        return value
    v = value.strip().lower()
    if v in {"la", "lista", "list", "lista_adjacencia", "1"}: return "LA"
    if v in {"ma", "matriz", "matriz_adjacencia", "matrix", "2"}: return "MA"
    return value.upper()

def _extract_vertices_from_filename(nome_arquivo: str):
    """
    Extrai V do nome do arquivo do grafo.
    Ex.: 'sample100-1980.gr' -> 100
    """
    m = re.search(r'(\d+)-', os.path.basename(str(nome_arquivo)))
    return int(m.group(1)) if m else None

# ============================
# Carregamento/Padronização
# ============================
def load_and_standardize_single(nome_csv):
    """
    Lê um CSV (ex.: dijkstra.csv) e padroniza para colunas:
      algoritmo, representacao, arquivo, V, tempo_ms
    - algoritmo: inferido do nome do CSV (ex.: 'dijkstra')
    - representacao: LA/MA (normalizada)
    - arquivo: nome do .gr
    - V: extraído do nome do arquivo .gr (sample100-... => 100)
    - tempo_ms: convertido para numérico
    """
    path = os.path.join(BASE_DIR, nome_csv)
    df = pd.read_csv(path)

    # Segundo seu formato: arquivo, rep, origem, tempo_ms
    arquivo_col = _guess_col(df, ["arquivo", "file", "graph", "grafo"])
    rep_col     = _guess_col(df, ["rep", "representacao", "repr"], required=False)
    tempo_col   = _guess_col(df, ["tempo_ms", "tempo", "ms", "time_ms", "duracao_ms"])

    alg_nome = os.path.splitext(os.path.basename(nome_csv))[0]  # p.ex.: 'dijkstra'

    # Normaliza representação
    if rep_col is None:
        rep_norm = None
    else:
        rep_norm = df[rep_col].astype(str).map(_normalize_rep)

    # Extrai V pelo nome do arquivo do grafo (coluna 'arquivo')
    v_col = df[arquivo_col].apply(_extract_vertices_from_filename)

    out = pd.DataFrame({
        "algoritmo": alg_nome,
        "representacao": rep_norm,
        "arquivo": df[arquivo_col].astype(str),
        "V": v_col,  # 100, 200, 500...
        "tempo_ms": pd.to_numeric(df[tempo_col], errors="coerce")
    }).dropna(subset=["tempo_ms", "V"]).reset_index(drop=True)

    return out

def merge_la_ma(a, b):
    # Se algum dos lados não tiver coluna de representação, assumimos LA/MA
    if a["representacao"].isna().all(): a["representacao"] = "LA"
    if b["representacao"].isna().all(): b["representacao"] = "MA"
    return pd.concat([a, b], ignore_index=True)

# ============================
# Geração de gráficos e estatísticas
# ============================
def save_boxplots_por_algoritmo(df):
    """
    Gera 3 boxplots por algoritmo, um para cada V (100, 200, 500)
    que existir no CSV: {algo}_V{V}_LA_vs_MA_boxplot.png
    """
    os.makedirs(PLOTS_DIR, exist_ok=True)

    for algo in sorted(df["algoritmo"].unique()):
        sub_algo = df[df["algoritmo"] == algo]

        # Valores de V detectados (100/200/500 etc.)
        valores_V = sorted(int(v) for v in sub_algo["V"].dropna().unique())
        if not valores_V:
            print(f"[AVISO] {algo}: nenhuma informação de V encontrada.")
            continue

        for V in valores_V:
            sub = sub_algo[sub_algo["V"] == V]
            sub = sub[sub["representacao"].isin(["LA", "MA"])]

            if sub["representacao"].nunique() < 2:
                print(f"[AVISO] {algo} (V={V}): faltam dados de LA/MA para boxplot.")
                continue

            la = sub[sub["representacao"] == "LA"]["tempo_ms"]
            ma = sub[sub["representacao"] == "MA"]["tempo_ms"]

            plt.figure(figsize=(8, 5))
            plt.title(f"{algo.upper()} — LA vs MA (V={V})")
            plt.boxplot([la, ma], labels=["LA", "MA"], showfliers=False)
            plt.ylabel("Tempo (ms)")
            plt.tight_layout()

            out = os.path.join(PLOTS_DIR, f"{algo}_V{V}_LA_vs_MA_boxplot.png")
            plt.savefig(out, dpi=120)
            plt.close()
            print("[OK] Boxplot salvo em", out)

def gerar_estatisticas(df):
    """
    Estatísticas por algoritmo, representação e V.
    """
    return df.groupby(["algoritmo", "representacao", "V"])["tempo_ms"].agg(
        media="mean",
        mediana="median",
        p95=lambda x: pd.Series(x).quantile(0.95),
        n="count"
    ).reset_index()

# ============================
# Main
# ============================
def main():
    print("=== TP2 - Boxplots LA vs MA ===")
    print(f"CSV serão buscados automaticamente em: {BASE_DIR}\n")
    print("1) Um CSV combinado (LA+MA juntos)")
    print("2) Dois CSVs separados (LA e MA)")

    modo = input("Escolha 1 ou 2: ").strip()

    if modo == "1":
        # lista e escolhe 1 arquivo dentro de out/production
        nome = escolher_arquivo("Escolha o CSV combinado: ")
        df = load_and_standardize_single(nome)

        # precisa ter a coluna de representação
        if df["representacao"].isna().any():
            print("[ERRO] O CSV combinado precisa ter coluna de representação (LA/MA).")
            return

    elif modo == "2":
        # escolhe dois arquivos separados
        print("\n=== Escolha o arquivo LA ===")
        nome_la = escolher_arquivo("Número do CSV LA: ")

        print("\n=== Escolha o arquivo MA ===")
        nome_ma = escolher_arquivo("Número do CSV MA: ")

        df_la = load_and_standardize_single(nome_la)
        df_ma = load_and_standardize_single(nome_ma)
        df = merge_la_ma(df_la, df_ma)

    else:
        print("Opção inválida.")
        return

    # normaliza labels LA/MA e filtra apenas essas duas
    df["representacao"] = df["representacao"].map(_normalize_rep)
    df = df[df["representacao"].isin(["LA", "MA"])]

    if df.empty:
        print("[ERRO] Não há linhas com representacao LA/MA após a normalização.")
        return

    # salva boxplots por algoritmo e por V (100/200/500)
    save_boxplots_por_algoritmo(df)

    # estatísticas resumo
    stats = gerar_estatisticas(df)
    stats_path = os.path.join(PLOTS_DIR, "estatisticas_resumo.csv")
    stats.to_csv(stats_path, index=False)

    print(f"\n[OK] Estatísticas geradas em {stats_path}")
    print(f"[OK] Imagens salvas em {PLOTS_DIR}/")
    print("[OK] Finalizado!")

if __name__ == "__main__":
    main()
