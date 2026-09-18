import sys
from pathlib import Path

import matplotlib.pyplot as plt
import pandas as pd

source = Path(sys.argv[1] if len(sys.argv) > 1 else "results/walk.csv")
target = Path(sys.argv[2]) if len(sys.argv) > 2 else source.with_suffix(".png")

walk = pd.read_csv(source)
evaluations = walk["evaluations"]

figure, cost = plt.subplots(figsize=(11, 6))
cost.plot(evaluations, walk["cost"], color="red", linewidth=0.4, label="costo aceptado")
cost.set_xlabel("evaluaciones")
cost.set_ylabel("costo")
cost.set_xlim(0, evaluations.max())
cost.set_ylim(0, None)
cost.legend(frameon=False, loc="upper right")
cost.spines["top"].set_visible(False)
cost.spines["right"].set_visible(False)

figure.tight_layout()
figure.savefig(target, dpi=150)
print(f"wrote {target}")
