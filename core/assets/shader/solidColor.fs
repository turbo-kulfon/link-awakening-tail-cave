varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
	vec4 tempColor = texture2D(u_texture, v_texCoords);
	if(tempColor[3] > 0.0) {
		gl_FragColor = v_color;
	}
	else {
		discard;
	}
}
