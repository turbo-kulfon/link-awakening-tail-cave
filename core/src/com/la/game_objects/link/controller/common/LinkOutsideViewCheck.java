package com.la.game_objects.link.controller.common;

import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;

public class LinkOutsideViewCheck implements OutsideViewCheckCallback {
	public interface LinkOutsideViewCheckDependency {
		void bounceLeft();
		void bounceRight();
		void bounceUp();
		void bounceDown();

		boolean isRecoil();
	}

	private LinkOutsideViewCheckDependency dependency;

	public LinkOutsideViewCheck(LinkOutsideViewCheckDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public void outsideLeft() {
		if(dependency.isRecoil() == true) {
			dependency.bounceLeft();
		}
	}
	@Override
	public void outsideRight() {
		if(dependency.isRecoil() == true) {
			dependency.bounceRight();
		}
	}
	@Override
	public void outsideUp() {
		if(dependency.isRecoil() == true) {
			dependency.bounceUp();
		}
	}
	@Override
	public void outsideDown() {
		if(dependency.isRecoil() == true) {
			dependency.bounceDown();
		}
	}
}
