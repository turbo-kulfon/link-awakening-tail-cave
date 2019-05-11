varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
	vec4 tempColor = texture2D(u_texture, v_texCoords);
	gl_FragColor = vec4(tempColor);
	if(tempColor.r >= 0.5019 && tempColor.r <= 0.502) {
		gl_FragColor = vec4(v_color.r, v_color.g, v_color.b, 1);
	}
	else if(tempColor.rgb == vec3(1, 1, 1)) {
		float modR = v_color.r / 1.0;
		float modG = v_color.g / 1.0;
		float modB = v_color.b / 1.0;
		gl_FragColor = vec4(0.5 + (0.5 * modR), 0.5 + (0.5 * modG), 0.5 + (0.5 * modB), 1);
	}
}
