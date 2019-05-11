varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
	vec4 tempColor = texture2D(u_texture, v_texCoords);
	if(tempColor[3] > 0.0) {
		float grey = 1.0 - ((tempColor.r + tempColor.g + tempColor.b)/3.0);
//		gl_FragColor = vec4((grey * 0.19), (grey * 0.59), (grey * 1.00), tempColor[3]);
		gl_FragColor = vec4((grey * v_color.r), (grey * v_color.g), (grey * v_color.b), tempColor[3]);
	}
	else {
		discard;
	}
}
