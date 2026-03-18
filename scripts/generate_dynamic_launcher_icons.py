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

import math
from pathlib import Path
from typing import Iterable

from PIL import Image, ImageDraw, ImageFont, ImageFilter

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


def _draw_robot_elements(draw: ImageDraw.ImageDraw, fill, offset_x=0, offset_y=0, alpha_mode=False) -> None:
    CX = ICON_SIZE // 2
    body_w = 176
    body_h = 166
    head_r = body_w // 2

    body_l = CX - body_w // 2 + offset_x
    body_r = CX + body_w // 2 + offset_x
    body_t = 164 + offset_y
    body_b = body_t + body_h

    # Head seamlessly attached to body
    head_b = body_t
    head_box_t = head_b - head_r
    head_box_b = head_b + head_r

    arm_w = 42
    arm_h = 114
    arm_gap = 14
    l_arm_l = body_l - arm_gap - arm_w
    l_arm_r = body_l - arm_gap
    r_arm_l = body_r + arm_gap
    r_arm_r = body_r + arm_gap + arm_w

    leg_w = 42
    leg_h = 76
    leg_gap = 42
    l_leg_l = CX - leg_gap//2 - leg_w + offset_x
    l_leg_r = CX - leg_gap//2 + offset_x
    r_leg_l = CX + leg_gap//2 + offset_x
    r_leg_r = CX + leg_gap//2 + leg_w + offset_x

    ant_l = 44
    ant_w = 9
    angle = 0.5235987756  # 30 degrees in radians
    head_cx = CX + offset_x
    head_cy = head_b

    # Antennas
    start_x = head_cx - head_r * math.sin(angle) * 0.75
    start_y = head_cy - head_r * math.cos(angle) * 0.75
    end_x = start_x - ant_l * math.sin(angle)
    end_y = start_y - ant_l * math.cos(angle)
    draw.line((start_x, start_y, end_x, end_y), fill=fill, width=ant_w)
    draw.ellipse((start_x - ant_w//2, start_y - ant_w//2, start_x + ant_w//2, start_y + ant_w//2), fill=fill)
    draw.ellipse((end_x - ant_w//2, end_y - ant_w//2, end_x + ant_w//2, end_y + ant_w//2), fill=fill)

    start_x_r = head_cx + head_r * math.sin(angle) * 0.75
    start_y_r = start_y
    end_x_r = start_x_r + ant_l * math.sin(angle)
    end_y_r = end_y
    draw.line((start_x_r, start_y_r, end_x_r, end_y_r), fill=fill, width=ant_w)
    draw.ellipse((start_x_r - ant_w//2, start_y_r - ant_w//2, start_x_r + ant_w//2, start_y_r + ant_w//2), fill=fill)
    draw.ellipse((end_x_r - ant_w//2, end_y_r - ant_w//2, end_x_r + ant_w//2, end_y_r + ant_w//2), fill=fill)

    # Legs
    draw.rounded_rectangle((l_leg_l, body_b - 20, l_leg_r, body_b + leg_h), radius=leg_w//2, fill=fill)
    draw.rounded_rectangle((r_leg_l, body_b - 20, r_leg_r, body_b + leg_h), radius=leg_w//2, fill=fill)

    # Arms
    draw.rounded_rectangle((l_arm_l, body_t, l_arm_r, body_t + arm_h), radius=arm_w//2, fill=fill)
    draw.rounded_rectangle((r_arm_l, body_t, r_arm_r, body_t + arm_h), radius=arm_w//2, fill=fill)

    # Body
    draw.rounded_rectangle((body_l, body_t, body_r, body_b), radius=22, fill=fill)
    # Fill the top corners to merge seamlessly with the head
    draw.rectangle((body_l, body_t, body_r, body_t + 22), fill=fill)

    # Head (perfect semi-circle)
    draw.pieslice((body_l, head_box_t, body_r, head_box_b), start=180, end=360, fill=fill)

    # Eyes
    if not alpha_mode:
        eye_r = 9
        eye_y = head_b - head_r * 0.45
        l_eye_x = head_cx - head_r * 0.45
        r_eye_x = head_cx + head_r * 0.45
        draw.ellipse((l_eye_x - eye_r, eye_y - eye_r, l_eye_x + eye_r, eye_y + eye_r), fill="#FFFFFF")
        draw.ellipse((r_eye_x - eye_r, eye_y - eye_r, r_eye_x + eye_r, eye_y + eye_r), fill="#FFFFFF")


def _draw_api_text_foreground(draw: ImageDraw.ImageDraw, text: str) -> None:
    font_size = 76 if text == "API" else 92
    font = get_font(font_size)
    CX = ICON_SIZE // 2
    # Center vertically on the main rectangle body
    center_y = 164 + 166 // 2
    badge_r = 64

    # Badge drop shadow
    draw.ellipse((CX - badge_r, center_y - badge_r + 6, CX + badge_r, center_y + badge_r + 6), fill=(0, 0, 0, 50))
    # Badge (White)
    draw.ellipse((CX - badge_r, center_y - badge_r, CX + badge_r, center_y + badge_r), fill="#FFFFFF")

    # Text (Android Navy for massive contrast against the white badge)
    draw.text((CX, center_y - 2), text, font=font, fill="#073042", anchor="mm")


def _draw_api_text_monochrome(draw: ImageDraw.ImageDraw, text: str) -> None:
    font_size = 76 if text == "API" else 92
    font = get_font(font_size)
    CX = ICON_SIZE // 2
    center_y = 164 + 166 // 2
    badge_r = 64

    # Cutout a hole in the robot body for the badge shape
    draw.ellipse((CX - badge_r, center_y - badge_r, CX + badge_r, center_y + badge_r), fill=0)

    # Draw solid text inside the hole
    draw.text((CX, center_y - 2), text, font=font, fill=255, anchor="mm")


def create_foreground_image(text: str) -> Image.Image:
    image = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))

    # Draw soft drop shadow layer for Material style
    shadow = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    _draw_robot_elements(shadow_draw, (0, 0, 0, 60), offset_x=0, offset_y=10)
    shadow = shadow.filter(ImageFilter.GaussianBlur(8))
    image.alpha_composite(shadow)

    draw = ImageDraw.Draw(image)
    _draw_robot_elements(draw, "#3DDC84", alpha_mode=False)
    _draw_api_text_foreground(draw, text)

    return image


def create_monochrome_image(text: str) -> Image.Image:
    alpha = Image.new("L", (ICON_SIZE, ICON_SIZE), 0)
    draw = ImageDraw.Draw(alpha)

    # Draw solid white robot
    _draw_robot_elements(draw, 255, alpha_mode=True)

    # Cutout eyes manually for alpha channel
    CX = ICON_SIZE // 2
    body_w = 176
    head_r = body_w // 2
    head_b = 164
    head_cx = CX
    eye_r = 9
    eye_y = head_b - head_r * 0.45
    l_eye_x = head_cx - head_r * 0.45
    r_eye_x = head_cx + head_r * 0.45
    draw.ellipse((l_eye_x - eye_r, eye_y - eye_r, l_eye_x + eye_r, eye_y + eye_r), fill=0)
    draw.ellipse((r_eye_x - eye_r, eye_y - eye_r, r_eye_x + eye_r, eye_y + eye_r), fill=0)

    # Cutout badge and draw text
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
