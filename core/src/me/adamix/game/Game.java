package me.adamix.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture tile;
	int scale = 2;
	float i_x = 1f;
	float i_y = 0.5f;
	float j_x = -1f;
	float j_y = 0.5f;
	int w = 32 * scale;
	int h = 32 * scale;
	Vector2 offset;

	int tileCountX = 20;
	int tileCountY = 20;
	int tileCountZ = 2;

	Vector2 selectedTilePos = new Vector2(0, 0);

	@Override
	public void create () {
		batch = new SpriteBatch();
		tile = new Texture("tile.png");
		offset = new Vector2((float) Gdx.graphics.getWidth() / 2, -550);
	}

	public Vector2 gridToScreen(Vector2 gridPos) {
		return new Vector2(
				gridPos.x * i_x * ((float) w / 2) + gridPos.y * j_x * ((float) w / 2),
				gridPos.x * i_y * ((float) h / 2) + gridPos.y * j_y * ((float) h / 2)
		);
	}

	public Map<String, Float> invertMatrix(float a, float b, float c, float d) {
		float det = (1 / (a * d - b * c));

		return Map.of(
				"a", det * d,
				"b", det * -b,
				"c", det * -c,
				"d", det * a
		);
	}

	public Vector2 screenToGrid(Vector2 screenPos) {
		float a = (float) (i_x * 0.5 * w);
		float b = (float) (j_x * 0.5 * w);
		float c = (float) (i_y * 0.5 * h);
		float d = (float) (j_y * 0.5 * h);

		Map<String, Float> invertedMatrix = invertMatrix(a, b, c, d);

		return new Vector2(
				(int) (screenPos.x * invertedMatrix.get("a") + screenPos.y * invertedMatrix.get("b")),
				(int) (screenPos.x * invertedMatrix.get("c") + screenPos.y * invertedMatrix.get("d"))
		);
	}

	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
			tileCountY++;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.C)) {
			tileCountY--;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
			tileCountX++;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			tileCountX--;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			offset.x += 10;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			offset.x += -10;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			offset.y += 10;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			offset.y += -10;
		}

		Vector2 mousePos = new Vector2(Gdx.input.getX() - offset.x, Gdx.input.getY() - offset.y);
		selectedTilePos = screenToGrid(mousePos);

	}

	@Override
	public void render () {
		handleInput();
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();

		for (int z = 0; z < tileCountZ; z++) {
			for (int y = 0; y < tileCountY; y++) {
				for (int x = 0; x < tileCountX; x++) {
					Vector2 pos = gridToScreen(new Vector2(x, y));
					int verticalOffset = 0;
					if (x == (int) selectedTilePos.x && y == (int) selectedTilePos.y) {
						verticalOffset = 10;
					}
					batch.draw(
							tile,
							pos.x + offset.x - (float) w / 2,
							Gdx.graphics.getHeight() - (pos.y + offset.y) - h + verticalOffset + (float) (z * h) / 2,
							w,
							h
					);
				}
			}
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
