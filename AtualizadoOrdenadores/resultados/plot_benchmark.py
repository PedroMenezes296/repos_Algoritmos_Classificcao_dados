#!/usr/bin/env python3
# -*- coding: utf-8 -*-

# =============== PARÂMETROS FIXOS ===============
# Filtrar por n (ou None para não filtrar)
N_FILTER = None          # ex.: 10000 ou None
# Filtrar por cenário (ou None). Valores típicos: "aleatorio", "crescente", "decrescente"
CENARIO_FILTER = None
# Escala do eixo Y: "linear" ou "log"
YSCALE = "linear"
# Título (None para gerar automaticamente)
TITLE = None
# Arquivo de saída do gráfico (será salvo em 'resultados/')
# OUTFILE = "saida.png"
# =================================================

import os, sys
import pandas as pd
import matplotlib.pyplot as plt

def ler_csv_automatico(path):
    """Tenta detectar delimitador: vírgula, ponto-e-vírgula ou tab."""
    for sep in [",", ";", "\t"]:
        try:
            df = pd.read_csv(path, sep=sep)
            if df.shape[1] >= 2:
                return df
        except Exception:
            pass
    raise RuntimeError("Não foi possível ler o CSV. Verifique o delimitador (vírgula/;).")

def main():
    print("=== Plot de Benchmark ===")
    csv_path = input("Informe o caminho do CSV (ex.: resultados/resultados.csv): ").strip()
    if not csv_path:
        print("Nada informado. Encerrando.")
        sys.exit(1)
    if not os.path.exists(csv_path):
        print(f"Arquivo não encontrado: {csv_path}")
        sys.exit(1)

    try:
        df = ler_csv_automatico(csv_path)
    except Exception as e:
        print("Erro lendo CSV:", e)
        sys.exit(1)

    # normaliza nomes (case-insensitive)
    cols = {c.lower(): c for c in df.columns}
    def tem(col): return col in cols
    def c(col): return cols[col]

    if not tem("algoritmo"):
        print("CSV precisa ter a coluna 'algoritmo'. Colunas encontradas:", list(df.columns))
        sys.exit(1)

    # filtros fixos
    if N_FILTER is not None and tem("n"):
        df = df[df[c("n")] == N_FILTER]
    if CENARIO_FILTER is not None and tem("cenario"):
        df = df[df[c("cenario")].astype(str).str.lower() == str(CENARIO_FILTER).lower()]
    if df.empty:
        print("Nenhum dado após aplicar filtros fixos (N_FILTER/CENARIO_FILTER).")
        sys.exit(1)
    OUTFILE = input("Informe o caminho do arquivo de saída (ex.: resultados/saida.png): ").strip()
    os.makedirs(os.path.dirname(OUTFILE) or ".", exist_ok=True)

    plt.figure(figsize=(10, 6))
    if tem("tempo_ms"):
        # Boxplot por algoritmo usando execuções individuais
        grupos, labels = [], []
        for alg, g in df.groupby(c("algoritmo")):
            valores = pd.to_numeric(g[c("tempo_ms")], errors="coerce").dropna().values
            if valores.size > 0:
                grupos.append(valores); labels.append(alg)

        if not grupos:
            print("Não há valores válidos em 'tempo_ms' para boxplot.")
            sys.exit(1)

        plt.boxplot(grupos, labels=labels, showmeans=True, meanline=True)
        plt.ylabel("Tempo (ms)")
        titulo = TITLE or "Boxplot por algoritmo"
    elif tem("media_ms"):
        # Sem execuções individuais → gráfico de barras com médias
        medias = (df.groupby(c("algoritmo"))[c("media_ms")]
                  .mean()
                  .sort_values(ascending=False))
        plt.bar(medias.index, medias.values)
        plt.ylabel("Média (ms)")
        titulo = TITLE or "Média por algoritmo"
    else:
        print("CSV não contém 'tempo_ms' nem 'media_ms'. Colunas:", list(df.columns))
        sys.exit(1)

    # complementa título com filtros
    
    if N_FILTER is not None: titulo += f" — n={N_FILTER}"
    if CENARIO_FILTER: titulo += f" — {CENARIO_FILTER}"
    plt.title(titulo)
    plt.xlabel("Algoritmo")
    plt.grid(True, axis="y", linestyle="--", alpha=0.4)
    plt.yscale(YSCALE)
    plt.tight_layout()
    plt.savefig(OUTFILE, dpi=140)
    print("Gráfico salvo em:", os.path.abspath(OUTFILE))

if __name__ == "__main__":
    main()
