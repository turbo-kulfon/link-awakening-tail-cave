package com.la.game_objects.pickup.instrument;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class InstrumentDrawComponent {
	public interface InstrumentDrawComponentDependency {
		float getX();
		float getY();
	}

	private TextureDrawComponent drawComponent;
	private int counter;
	private float r, g, b;
	private InstrumentDrawComponentDependency dependency;

	public InstrumentDrawComponent(GFXSystem gfxSystem,
			int texX, int texY, int texW, int texH, int layer,
			InstrumentDrawComponentDependency dependency) {
		this.dependency = dependency;

		drawComponent = gfxSystem.createTextureDrawComponent(layer);
		drawComponent.setTexture(texX, texY, texW, texH);
		drawComponent.setSize(texW, texH);

		update();
	}

	public void update() {
		counter += 1;
		if(counter >= 169) {
			counter = 0;
		}
		float mod = counter/169.0f;
		int frame = (int) Math.floor(13 * mod);
		if(frame == 0) {
			r =   0; g = 136/255.0f; b = 112/255.0f;
		}
		else if(frame == 1) {
			r =   0; g = 104/255.0f; b = 160/255.0f;
		}
		else if(frame == 2) {
			r =  64/255.0f; g =  64/255.0f; b = 248/255.0f;
		}
		else if(frame == 3) {
			r = 128/255.0f; g =  16/255.0f; b = 184/255.0f;
		}
		else if(frame == 4) {
			r = 144/255.0f; g =   0; b = 128/255.0f;
		}
		else if(frame == 5) {
			r = 168/255.0f; g =   0; b =  96/255.0f;
		}
		else if(frame == 6) {
			r = 184/255.0f; g =   0; b =  40/255.0f;
		}
		else if(frame == 7) {
			r = 184/255.0f; g =  48/255.0f; b =   8/255.0f;
		}
		else if(frame == 8) {
			r = 184/255.0f; g =  72/255.0f; b =   8/255.0f;
		}
		else if(frame == 9) {
			r = 168/255.0f; g = 120/255.0f; b =  16/255.0f;
		}
		else if(frame == 10) {
			r = 128/255.0f; g = 128/255.0f; b =  16/255.0f;
		}
		else if(frame == 11) {
			r =  88/255.0f; g = 144/255.0f; b =   8/255.0f;
		}
		else if(frame == 12) {
			r =   0; g = 169/255.0f; b =   8/255.0f;
		}
	}
	public void draw() {
		drawComponent.setPosition(dependency.getX(), dependency.getY());
		drawComponent.setInstrumentMode(true, r, g, b);
	}
	public void remove() {
		drawComponent.remove();
	}
}
