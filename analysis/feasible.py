import sys
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

FEASIBLE = 1.0
BINS = 60
RESULTS = Path("results")


def read(source):
    runs = pd.read_csv(source, comment="#")
    return runs["cost"] if "cost" in runs.columns else None


def summary(values):
    return {
        "mejor": f"{values.min():.8f}",
        "mediana": f"{values.median():.8f}",
        "promedio": f"{values.mean():.8f}",
        "peor": f"{values.max():.8f}",
        "desviacion": f"{values.std():.8f}",
    }


def histogram(values, target):
    figure, axes = plt.subplots(figsize=(9, 5))
    axes.hist(values, bins=BINS, color="steelblue")
    axes.axvline(values.min(), color="red", linewidth=1.2,
                 label=f"mejor {values.min():.6f}")
    axes.axvline(values.median(), color="black", linewidth=1.2, linestyle="--",
                 label=f"mediana {values.median():.6f}")
    axes.set_xlabel("costo")
    axes.set_ylabel("corridas")
    axes.legend(frameon=False)
    axes.spines["top"].set_visible(False)
    axes.spines["right"].set_visible(False)
    figure.tight_layout()
    figure.savefig(target, dpi=150)
    plt.close(figure)
    return target


def report(source):
    costs = read(source)
    if costs is None:
        print(f"{source}: no hay columna 'cost', se omite")
        return

    feasible = costs[costs <= FEASIBLE]
    print(source)
    print(f"  {'factibles':<12}{len(feasible):,} de {len(costs):,} "
          f"({len(feasible) / len(costs):.1%})")
    if feasible.empty:
        return

    for name, value in summary(feasible).items():
        print(f"  {name:<12}{value}")
    RESULTS.mkdir(exist_ok=True)
    print(f"  {'grafica':<12}{histogram(feasible, RESULTS / f'{source.stem}-dist.png')}")


for argument in sys.argv[1:] or sorted(RESULTS.glob("*.csv")):
    report(Path(argument))
