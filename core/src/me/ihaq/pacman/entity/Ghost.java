package me.ihaq.pacman.entity;

import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.ihaq.pacman.Main;
import me.ihaq.pacman.menu.Game.FACING;
import me.ihaq.pacman.utils.CollisionRect;
import me.ihaq.pacman.utils.Intersection;
import me.ihaq.pacman.utils.Portal;

public class Ghost {

	private int x, initialX, initialY, y, height, width, vx, vy;
	private Sprite ghost, eatGhost;
	private FACING facing;
	private boolean alive, eatable;
	private CollisionRect rect;

	public Ghost(Texture t, int x, int y) {
		this.ghost = new Sprite(t);
		this.eatGhost = new Sprite(new Texture("game/ghostEAT.png"));
		this.x = x;
		this.y = y;
		this.initialX = x;
		this.initialY = y;
		this.width = t.getWidth();
		this.height = t.getHeight();
		this.facing = FACING.UP;
		this.alive = true;
		this.rect = new CollisionRect(x, y, x + this.width, y + this.height);
	}

	public void render(SpriteBatch batch) {
		if (!alive) {
			alive = true;
			this.x = initialX;
			this.y = initialY;
		}
		if (!eatable) {
			this.ghost.setPosition(this.x, this.y);
			this.ghost.draw(batch);
		} else {
			this.eatGhost.setPosition(this.x, this.y);
			this.eatGhost.draw(batch);
		}

		this.rect = new CollisionRect(this.x, this.y, this.x + this.width, this.y + this.height);

		if (!Main.GAME.playing) {
			return;
		}

		if (this.facing == FACING.UP && !collides(this.x, this.y + 2)) {
			this.vx = 0;
			this.vy = 2;

			this.x += this.vx;
			this.y += this.vy;
		}

		else if (this.facing == FACING.DOWN && !collides(this.x, this.y - 2)) {
			this.vx = 0;
			this.vy = -2;

			this.x += this.vx;
			this.y += this.vy;
		}

		else if (this.facing == FACING.RIGHT && !collides(this.x + 2, this.y)) {
			this.vx = 2;
			this.vy = 0;

			this.x += this.vx;
			this.y += this.vy;
		}

		else if (this.facing == FACING.LEFT && !collides(this.x - 2, this.y)) {
			this.vx = -2;
			this.vy = 0;

			this.x += this.vx;
			this.y += this.vy;
		}

		checkForPortals();
		checkForCollisions();

	}

	private void checkForCollisions() {
		this.facing = intersectionCollide() ? newDirection() : this.facing;
		this.facing = intersectionCollide() ? newDirection() : this.facing;
		this.facing = intersectionCollide() ? newDirection() : this.facing;
		this.facing = intersectionCollide() ? newDirection() : this.facing;
	}

	private void checkForPortals() {
		for (Portal r : Main.GAME.portals) {
			if (r.getCollisionRect().collidesWith(this.rect)) {
				this.x = r.getTargetX();
			}
		}
	}

	private boolean collides(int x, int y) {
		CollisionRect pac = new CollisionRect(x, y, x + this.width, y + this.height);
		for (CollisionRect r : Main.GAME.boxes) {
			if (r.collidesWith(pac)) {
				return true;
			}
		}
		return false;
	}

	private boolean intersectionCollide() {
		for (Intersection r : Main.GAME.intersections) {
			if (r.getCollisionRect().collidesWith(this.rect)) {
				return true;
			}
		}
		return false;
	}

	private Intersection getCollidingIntersection() {
		for (Intersection r : Main.GAME.intersections) {
			if (r.getCollisionRect().collidesWith(this.rect)) {
				return r;
			}
		}
		return null;
	}

	private FACING newDirection() {
		Intersection i = getCollidingIntersection();
		int newMove = new Random().nextInt(i.getDirections().size() - 1);
		System.out.println(i.getDirections().get(newMove));
		return i.getDirections().get(newMove);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public CollisionRect getCollisionRect() {
		return this.rect;
	}

	public void setAlive(boolean b) {
		this.alive = b;
	}

	public boolean isAlive() {
		return this.alive;
	}

	public void setEatable(boolean b) {
		this.eatable = b;
	}

	public boolean isEatable() {
		return this.eatable;
	}
}