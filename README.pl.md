# No Flame & Smoke

Wyłączaj **płomień** i **dym** dla pochodni, świec i pieców — per-blok lub globalnie.  
Działa na **Fabric Loader** dla wersji **Minecraft 1.21.x**. **Fabric API nie jest wymagane.**

---

## ✨ Funkcje

- Przełączniki *Flame* i *Smoke* dla:
    - zwykłych, redstone, soul oraz (przyszłościowo) copper torches — także wersji ściennych
    - świec (candles)
    - pieców: **Furnace**, **Smoker**, **Blast Furnace**
- Globalne „**All Flames** / **All Smoke**” + „**Apply to all**” i „**Restore vanilla**”
- Listę można przewijać; scrollbar korzysta z **vanilla** tekstury (`sprites/widget/scroller.png`), więc automatycznie pasuje do paczek zasobów.

> **UI semantyka:**  
> „**ON**” = efekt widoczny (vanilla), „**OFF**” = efekt ukryty przez moda.

---

## 🧭 Jak otworzyć ekran konfiguracji

- przez **Mod Menu** ➜ *No Flame & Smoke* ➜ **Config**  
  *(jeśli masz Mod Menu)*
- lub inną drogą, jeśli dodałeś wejście do swojego menu (mod udostępnia własny `NFSConfigScreen`).

Plik konfiguracyjny: `config/noflamesmoke.json`

Przykład (skrócony):
```json
{
  "disableAllFlames": false,
  "disableAllSmoke": false,
  "torch": { "flame": false, "smoke": false },
  "candles": { "flame": true, "smoke": false },
  "furnace": { "flame": false, "smoke": true }
}