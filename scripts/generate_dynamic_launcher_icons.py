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


def _draw_robot_base(draw: ImageDraw.ImageDraw, fill, alpha_mode: bool) -> None:
    draw.rounded_rectangle((124, 168, 308, 314), radius=42, fill=fill)
    draw.rounded_rectangle((88, 188, 126, 296), radius=20, fill=fill)
    draw.rounded_rectangle((306, 188, 344, 296), radius=20, fill=fill)
    draw.rounded_rectangle((156, 296, 194, 372), radius=18, fill=fill)
    draw.rounded_rectangle((238, 296, 276, 372), radius=18, fill=fill)
    draw.ellipse((136, 72, 296, 214), fill=fill)
    draw.rectangle((136, 144, 296, 214), fill=fill)
    draw.line((166, 84, 142, 44), fill=fill, width=10)
    draw.line((266, 84, 290, 44), fill=fill, width=10)

    if not alpha_mode:
        draw.ellipse((184, 126, 198, 140), fill="#FFFFFF")
        draw.ellipse((234, 126, 248, 140), fill="#FFFFFF")


def _draw_badge(draw: ImageDraw.ImageDraw, fill) -> tuple[int, int, int, int]:
    badge = (154, 214, 278, 272)
    draw.rounded_rectangle(badge, radius=28, fill=fill)
    return badge


def _draw_centered_text(draw: ImageDraw.ImageDraw, text: str, box: tuple[int, int, int, int], fill, cutout: bool = False) -> None:
    font_size = 60 if len(text) >= 3 else 72
    font = get_font(font_size)
    left, top, right, bottom = box
    center_x = (left + right) / 2
    center_y = (top + bottom) / 2 - 4
    if cutout:
        draw.text((center_x, center_y), text, font=font, fill=0, anchor="mm")
    else:
        draw.text((center_x, center_y), text, font=font, fill=fill, anchor="mm")


def create_foreground_image(text: str) -> Image.Image:
    image = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    draw = ImageDraw.Draw(image)

    shadow = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    shadow_draw.rounded_rectangle((118, 164, 314, 320), radius=46, fill=(15, 23, 42, 36))
    shadow_draw.ellipse((130, 68, 302, 220), fill=(15, 23, 42, 30))
    image.alpha_composite(shadow)

    _draw_robot_base(draw, "#3DDC84", alpha_mode=False)
    badge = _draw_badge(draw, "#0F172A")
    _draw_centered_text(draw, text, badge, "#FFFFFF")
    return image


def create_monochrome_image(text: str) -> Image.Image:
    alpha = Image.new("L", (ICON_SIZE, ICON_SIZE), 0)
    draw = ImageDraw.Draw(alpha)

    _draw_robot_base(draw, 255, alpha_mode=True)
    badge = _draw_badge(draw, 255)
    draw.ellipse((184, 126, 198, 140), fill=0)
    draw.ellipse((234, 126, 248, 140), fill=0)
    _draw_centered_text(draw, text, badge, 255, cutout=True)

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

