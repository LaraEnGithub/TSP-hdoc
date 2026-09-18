import sys
from pathlib import Path

import pandas as pd

FEASIBLE = 1.0

sources = [Path(argument) for argument in sys.argv[1:]] or sorted(
    Path("results").glob("*.csv"))

for source in sources:
    runs = pd.read_csv(source, comment="#")
    if "cost" not in runs.columns:
        print(f"{source}: sin columna 'cost', se omite")
        continue

    feasible = runs[runs["cost"] <= FEASIBLE]
    print(f"{source}")
    print(f"  corridas    {len(runs):,}")
    print(f"  factibles   {len(feasible):,} ({100 * len(feasible) / len(runs):.1f}%)")
    if not feasible.empty:
        print(f"  mejor       {feasible['cost'].min():.8f}")
        print(f"  mediana     {feasible['cost'].median():.8f}")
        print(f"  promedio    {feasible['cost'].mean():.8f}")
        print(f"  peor        {feasible['cost'].max():.8f}")
