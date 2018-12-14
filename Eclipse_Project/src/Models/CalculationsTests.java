package Models;

import Controllers.*;
import static org.junit.Assert.*;

import org.junit.Test;

// These JUnit tests serve to prove that the redeclaration-calculations work.
// The tests may also serve to show how the Runway objects and Obstruction objects can be used within the system.

public class CalculationsTests {

	@Test
	public void calculationTest1a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(750.0, runway.getRightLandLogicalRunway().resa, 0.0001);
		assertEquals(1205.0, runway.getRightLandLogicalRunway().lda, 0.0001);
		assertEquals(1235.0, runway.getRightLandLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest1b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 4.78, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getRightLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(1715.0, runway.getRightLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest2a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(750.0, runway.getRightTakeOffLogicalRunway().resa,
		// 0.0001);
		assertEquals(1765.0, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(1815.0, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(1795.0, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest2b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 3.2, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getRightTakeOffLogicalRunway().resa,
		// 0.0001);
		assertEquals(1765.0, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(1815.0, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(1795.0, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest3a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(1715.0, runway.getLeftLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest3b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 3.2, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(1715.0, runway.getLeftLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest4a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(750.0, runway.getLeftTakeOffLogicalRunway().resa, 0.0001);
		assertEquals(1205.0, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(1205.0, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(1205.0, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest4b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 3.2, 10.0, 2050.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getLeftTakeOffLogicalRunway().resa, 0.0001);
		assertEquals(1715.0, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(1715.0, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(1715.0, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest5a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(750.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(2220.0, runway.getLeftLandLogicalRunway().lda, 0.0001);
		assertEquals(2250.0, runway.getLeftLandLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest5b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 3.2, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(2730.0, runway.getLeftLandLogicalRunway().lda, 0.0001);
		assertEquals(2760.0, runway.getLeftLandLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest6a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getRightLandLogicalRunway().resa, 0.0001);
		assertEquals(2730.0, runway.getRightLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest6b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 3.2, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getRightLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(2730.0, runway.getRightLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest7a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(750.0, runway.getLeftTakeOffLogicalRunway().resa,
		// 0.0001);
		assertEquals(2630.0, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(2680.0, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(2660.0, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest7b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.3, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getLeftTakeOffLogicalRunway().resa,
		// 0.0001);
		assertEquals(2630.0, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(2680.0, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(2660.0, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest8a() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(750.0, runway.getRightTakeOffLogicalRunway().resa, 0.0001);
		assertEquals(2220.0, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(2220.0, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(2220.0, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest8b() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 250.0);
		Obstruction o = new Obstruction("Test", 9.0, 4.3, 10.0, 25.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		assertEquals(240.0, runway.getRightTakeOffLogicalRunway().resa, 0.0001);
		assertEquals(2730.0, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(2730.0, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(2730.0, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest9() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, -600.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getParametersApproachFromLeft().resa,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().tora, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().toda, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().asda, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest10() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 300.0);
		runway.setBlastDistance(300);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, 4000.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getParametersApproachFromLeft().resa,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().tora, runway.getLeftTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().toda, runway.getLeftTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().asda, runway.getLeftTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest11() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, -600.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(runway.getOriginalLogicalRunwayFromLeft().resa,
		// runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().lda, runway.getLeftLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest12() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, 4000.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(runway.getOriginalLogicalRunwayFromLeft().resa,
		// runway.getLeftLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromLeft().lda, runway.getLeftLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest13() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, -600.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getParametersApproachFromLeft().resa,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().tora, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().toda, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().asda, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest14() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, 4000.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(240.0, runway.getParametersApproachFromLeft().resa,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().tora, runway.getRightTakeOffLogicalRunway().tora, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().toda, runway.getRightTakeOffLogicalRunway().toda, 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().asda, runway.getRightTakeOffLogicalRunway().asda, 0.0001);
	}

	@Test
	public void calculationTest15() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, -600.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(runway.getOriginalLogicalRunwayFromRight().resa,
		// runway.getRightLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().lda, runway.getRightLandLogicalRunway().lda, 0.0001);
	}

	@Test
	public void calculationTest16() throws Exception {
		Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0, 3060.0, 3030.0, 3030.0, 400.0);
		runway.setBlastDistance(400);
		Obstruction o = new Obstruction("Test", 9.0, 4.0, 10.0, 4000.0, 0.0);
		runway.addObstruction(o);
		runway.redeclareParameters();
		// assertEquals(runway.getOriginalLogicalRunwayFromRight().resa,
		// runway.getRightLandLogicalRunway().resa, 0.0001);
		// assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
		// 0.0001);
		assertEquals(runway.getOriginalLogicalRunwayFromRight().lda, runway.getRightLandLogicalRunway().lda, 0.0001);
	}

	// @Test
	// public void calculationTestX1() throws Exception {
	// Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0,
	// 3060.0, 3030.0, 3030.0, 250.0);
	// Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 1500.0);
	// runway.addObstruction(o);
	// runway.redeclareParameters();
	// assertEquals(240.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
	// // assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
	// // 0.0001);
	// assertEquals(runway.getOriginalLogicalRunwayFromLeft().lda,
	// runway.getLeftLandLogicalRunway().lda, 0.0001);
	// }
	//
	// @Test
	// public void calculationTestX2() throws Exception {
	// Runway runway = new Runway("Test", 0, 3080.0, 3080.0, 3030, 3060.0,
	// 3060.0, 3030.0, 3030.0, 250.0);
	// Obstruction o = new Obstruction("Test", 9.0, 15.0, 10.0, 2050.0, 500.0);
	// runway.addObstruction(o);
	// runway.redeclareParameters();
	// assertEquals(240.0, runway.getLeftLandLogicalRunway().resa, 0.0001);
	// // assertEquals(1244.0, runway.parametersLandTakeOffFromRight.ldaEnd,
	// // 0.0001);
	// assertEquals(runway.getOriginalLogicalRunwayFromLeft().lda,
	// runway.getLeftLandLogicalRunway().lda, 0.0001);
	// }

}
