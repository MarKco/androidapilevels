#!/usr/bin/env python3
"""Generate launcher icon assets that show the current device API level.

This script produces:
- foreground PNGs in app/src/main/res/drawable-nodpi
- monochrome PNGs in app/src/main/res/drawable-nodpi
- legacy launcher wrappers in app/src/main/res/drawable
- adaptive icon wrappers in app/src/main/res/drawable-v26

Run it whenever a new Android API level should get a dedicated launcher icon.
"""

from __future__ import annotations

from pathlib import Path
from typing import Iterable

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
RES_DIR = ROOT / "app" / "src" / "main" / "res"
DRAWABLE_DIR = RES_DIR / "drawable"
DRAWABLE_V26_DIR = RES_DIR / "drawable-v26"
DRAWABLE_NODPI_DIR = RES_DIR / "drawable-nodpi"
FONT_PATH = RES_DIR / "font" / "roboto.ttf"
LEVELS = list(range(23, 37))
ICON_SIZE = 432
SAFE_AREA_DP = 72


def ensure_directories(paths: Iterable[Path]) -> None:
    for path in paths:
        path.mkdir(parents=True, exist_ok=True)


def get_font(size: int) -> ImageFont.FreeTypeFont | ImageFont.ImageFont:
    try:
        return ImageFont.truetype(str(FONT_PATH), size=size)
    except OSError:
        return ImageFont.load_default()


def _api_font_size(text: str) -> int:
    if len(text) <= 2:
        return 174
    if text == "API":
        return 122
    return 148


def _fg_xy(x: float, y: float) -> tuple[float, float]:
    # Matches the transform used by res/drawable/ic_launcher_foreground.xml
    scale = 2.61 * 4.0
    translate = 22.68 * 4.0
    return (translate + (x * scale), translate + (y * scale))


def _draw_robot_base(draw: ImageDraw.ImageDraw, fill, alpha_mode: bool) -> None:
    # Use official launcher-foreground proportions (24dp Android icon grid).
    x5, y16 = _fg_xy(5.0, 16.0)
    x19, y20 = _fg_xy(19.0, 20.0)
    x7, _ = _fg_xy(7.0, 16.0)
    _, y12 = _fg_xy(5.0, 12.0)
    _, y17 = _fg_xy(5.0, 17.0)
    _, y10 = _fg_xy(5.0, 10.0)
    _, y3 = _fg_xy(5.0, 3.0)
    _, y6 = _fg_xy(5.0, 6.0)

    body_radius = int((x7 - x5) * 0.42)
    limb_radius = int((x7 - x5) * 0.55)

    draw.rounded_rectangle((x5, y12, x19, y20), radius=body_radius, fill=fill)
    draw.rounded_rectangle((_fg_xy(3.0, 12.2)[0], _fg_xy(3.0, 12.2)[1], _fg_xy(5.0, 18.2)[0], _fg_xy(5.0, 18.2)[1]), radius=limb_radius, fill=fill)
    draw.rounded_rectangle((_fg_xy(19.0, 12.2)[0], _fg_xy(19.0, 12.2)[1], _fg_xy(21.0, 18.2)[0], _fg_xy(21.0, 18.2)[1]), radius=limb_radius, fill=fill)

    draw.rounded_rectangle((_fg_xy(7.2, 18.0)[0], _fg_xy(7.2, 18.0)[1], _fg_xy(9.4, 22.4)[0], _fg_xy(9.4, 22.4)[1]), radius=limb_radius, fill=fill)
    draw.rounded_rectangle((_fg_xy(14.6, 18.0)[0], _fg_xy(14.6, 18.0)[1], _fg_xy(16.8, 22.4)[0], _fg_xy(16.8, 22.4)[1]), radius=limb_radius, fill=fill)

    draw.pieslice((_fg_xy(5.0, 3.0)[0], y3, _fg_xy(19.0, 17.0)[0], y17), start=180, end=360, fill=fill)
    draw.rectangle((x5, y10, x19, y17), fill=fill)
    draw.line((_fg_xy(7.88, 4.37)[0], _fg_xy(7.88, 4.37)[1], _fg_xy(6.0, 2.27)[0], _fg_xy(6.0, 2.27)[1]), fill=fill, width=10)
    draw.line((_fg_xy(16.12, 4.37)[0], _fg_xy(16.12, 4.37)[1], _fg_xy(18.22, 2.27)[0], _fg_xy(18.22, 2.27)[1]), fill=fill, width=10)

    if not alpha_mode:
        ex1, ey1 = _fg_xy(9.0, 9.0)
        ex2, ey2 = _fg_xy(15.0, 9.0)
        eye_r = 8
        draw.ellipse((ex1 - eye_r, ey1 - eye_r, ex1 + eye_r, ey1 + eye_r), fill="#FFFFFF")
        draw.ellipse((ex2 - eye_r, ey2 - eye_r, ex2 + eye_r, ey2 + eye_r), fill="#FFFFFF")


def _draw_api_text_foreground(draw: ImageDraw.ImageDraw, text: str) -> None:
    font = get_font(_api_font_size(text))
    center = (216, 262)

    # Material-like chip behind the number for stable contrast.
    draw.rounded_rectangle((44, 176, 388, 334), radius=78, fill=(15, 23, 42, 170))

    # Soft outer glow keeps digits readable over busy launchers/masks.
    draw.text(center, text, font=font, fill=(8, 16, 30, 120), anchor="mm", stroke_width=24, stroke_fill=(8, 16, 30, 120))
    draw.text(center, text, font=font, fill="#FFFFFF", anchor="mm", stroke_width=12, stroke_fill="#0F172A")


def _draw_api_text_monochrome(draw: ImageDraw.ImageDraw, text: str) -> None:
    font = get_font(_api_font_size(text))
    center = (216, 262)

    # Wide support shape keeps the cutout digits readable in single-color mode.
    draw.rounded_rectangle((44, 176, 388, 334), radius=78, fill=255)
    draw.text(center, text, font=font, fill=0, anchor="mm")


def create_foreground_image(text: str) -> Image.Image:
    image = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)

    shadow = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    shadow_draw.rounded_rectangle((138, 202, 294, 344), radius=44, fill=(15, 23, 42, 46))
    shadow_draw.pieslice((136, 118, 296, 286), start=180, end=360, fill=(15, 23, 42, 36))
    image.alpha_composite(shadow)

    _draw_robot_base(draw, "#2660A4", alpha_mode=False)
    _draw_api_text_foreground(draw, text)
    return image


def create_monochrome_image(text: str) -> Image.Image:
    alpha = Image.new("L", (ICON_SIZE, ICON_SIZE), 0)
    draw = ImageDraw.Draw(alpha)

    _draw_robot_base(draw, 255, alpha_mode=True)
    ex1, ey1 = _fg_xy(9.0, 9.0)
    ex2, ey2 = _fg_xy(15.0, 9.0)
    eye_r = 8
    draw.ellipse((ex1 - eye_r, ey1 - eye_r, ex1 + eye_r, ey1 + eye_r), fill=0)
    draw.ellipse((ex2 - eye_r, ey2 - eye_r, ex2 + eye_r, ey2 + eye_r), fill=0)
    _draw_api_text_monochrome(draw, text)

    image = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    image.putalpha(alpha)
    black = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 255))
    return Image.composite(black, Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0)), alpha)


def write_icon_wrappers(name: str, fg_res: str, mono_res: str) -> None:
    legacy_xml = f'''<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:drawable="@color/ic_launcher_background" />
    <item
        android:width="{SAFE_AREA_DP}dp"
        android:height="{SAFE_AREA_DP}dp"
        android:gravity="center"
        android:drawable="@drawable/{fg_res}" />
</layer-list>
'''
    adaptive_xml = f'''<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@color/ic_launcher_background" />
    <foreground android:drawable="@drawable/{fg_res}" />
    <monochrome android:drawable="@drawable/{mono_res}" />
</adaptive-icon>
'''
    (DRAWABLE_DIR / f"{name}.xml").write_text(legacy_xml, encoding="utf-8")
    (DRAWABLE_V26_DIR / f"{name}.xml").write_text(adaptive_xml, encoding="utf-8")


def main() -> None:
    ensure_directories([DRAWABLE_DIR, DRAWABLE_V26_DIR, DRAWABLE_NODPI_DIR])

    variants = {"ic_launcher_default": "API"}
    variants.update({f"ic_launcher_api_{level}": str(level) for level in LEVELS})

    for name, text in variants.items():
        fg_name = f"{name}_fg"
        mono_name = f"{name}_mono"
        create_foreground_image(text).save(DRAWABLE_NODPI_DIR / f"{fg_name}.png")
        create_monochrome_image(text).save(DRAWABLE_NODPI_DIR / f"{mono_name}.png")
        write_icon_wrappers(name, fg_name, mono_name)


if __name__ == "__main__":
    main()

