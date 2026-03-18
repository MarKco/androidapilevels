import sys
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont, ImageFilter
import math

ROOT = Path(__file__).resolve().parents[1]
RES_DIR = ROOT / "app" / "src" / "main" / "res"
FONT_PATH = RES_DIR / "font" / "roboto.ttf"
ICON_SIZE = 432

def get_font(size: int):
    try:
        return ImageFont.truetype(str(FONT_PATH), size=size)
    except OSError:
        return ImageFont.load_default()

def _draw_robot_elements(draw, fill, offset_x=0, offset_y=0, alpha_mode=False):
    CX = ICON_SIZE // 2
    body_w = 170
    body_h = 150
    body_l = CX - body_w // 2 + offset_x
    body_r = CX + body_w // 2 + offset_x
    body_t = 160 + offset_y
    body_b = body_t + body_h

    head_gap = 12
    head_b = body_t - head_gap
    head_t = head_b - body_w

    arm_w = 40
    arm_h = 110
    arm_gap = 16
    l_arm_l = body_l - arm_gap - arm_w
    l_arm_r = body_l - arm_gap
    r_arm_l = body_r + arm_gap
    r_arm_r = body_r + arm_gap + arm_w

    leg_w = 40
    leg_h = 70
    leg_gap = 30
    l_leg_l = CX - leg_gap//2 - leg_w + offset_x
    l_leg_r = CX - leg_gap//2 + offset_x
    r_leg_l = CX + leg_gap//2 + offset_x
    r_leg_r = CX + leg_gap//2 + leg_w + offset_x

    ant_l = 40
    ant_w = 8
    angle = 0.5235987756
    head_cx = CX + offset_x
    head_cy = head_b
    head_r = body_w // 2

    # Antennas
    start_x = head_cx - head_r * math.sin(angle) * 0.7
    start_y = head_cy - head_r * math.cos(angle) * 0.7
    end_x = start_x - ant_l * math.sin(angle)
    end_y = start_y - ant_l * math.cos(angle)
    draw.line((start_x, start_y, end_x, end_y), fill=fill, width=ant_w)
    draw.ellipse((start_x - ant_w//2, start_y - ant_w//2, start_x + ant_w//2, start_y + ant_w//2), fill=fill)
    draw.ellipse((end_x - ant_w//2, end_y - ant_w//2, end_x + ant_w//2, end_y + ant_w//2), fill=fill)

    start_x_r = head_cx + head_r * math.sin(angle) * 0.7
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
    draw.rounded_rectangle((body_l, body_t, body_r, body_b), radius=20, fill=fill)
    draw.rectangle((body_l, body_t, body_r, body_t + 20), fill=fill)

    # Head
    draw.pieslice((body_l, head_t, body_r, head_b + head_r), start=180, end=360, fill=fill)

    # Eyes
    if not alpha_mode:
        eye_r = 8
        eye_y = head_b - head_r * 0.45
        l_eye_x = head_cx - head_r * 0.4
        r_eye_x = head_cx + head_r * 0.4
        draw.ellipse((l_eye_x - eye_r, eye_y - eye_r, l_eye_x + eye_r, eye_y + eye_r), fill="#FFFFFF")
        draw.ellipse((r_eye_x - eye_r, eye_y - eye_r, r_eye_x + eye_r, eye_y + eye_r), fill="#FFFFFF")

def _draw_api_text_foreground(draw, text):
    font_size = 64 if text == "API" else 76
    font = get_font(font_size)
    CX = ICON_SIZE // 2
    center_y = 160 + 150 // 2
    badge_r = 52
    draw.ellipse((CX - badge_r, center_y - badge_r + 4, CX + badge_r, center_y + badge_r + 4), fill=(0, 0, 0, 40))
    draw.ellipse((CX - badge_r, center_y - badge_r, CX + badge_r, center_y + badge_r), fill="#FFFFFF")
    draw.text((CX, center_y - 2), text, font=font, fill="#3DDC84", anchor="mm")

def create_foreground_image(text: str) -> Image.Image:
    image = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    shadow = Image.new("RGBA", (ICON_SIZE, ICON_SIZE), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    _draw_robot_elements(shadow_draw, (0, 0, 0, 60), offset_x=0, offset_y=10)
    shadow = shadow.filter(ImageFilter.GaussianBlur(8))
    image.alpha_composite(shadow)

    draw = ImageDraw.Draw(image)
    _draw_robot_elements(draw, "#3DDC84", alpha_mode=False)
    _draw_api_text_foreground(draw, text)
    return image

if __name__ == "__main__":
    img = create_foreground_image("35")
    img.save(ROOT / "scripts" / "test_icon.png")
    print("Test icon saved.")
