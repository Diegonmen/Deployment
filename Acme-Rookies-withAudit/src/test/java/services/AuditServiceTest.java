
package services;

import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Audit;
import domain.Position;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AuditServiceTest extends AbstractTest {

	//Test coverage: 100%  // Covered instructions: 335 // Missed instructions: 0 // Total Instructions: 335
	//AuditService coverage: 76%  // Covered instructions: 98 // Missed instructions: 31 // Total Instructions: 129
	
	@Autowired
	private AuditService	auditService;

	@Autowired
	private PositionService	positionService;


	@Test
	public void selfAssignAndSaveAuditDriver() {
		Object testingData[][] = {
			{
				"rookie0", "Olaci", 5.0, IllegalArgumentException.class
				// Rookies cannot create audits
			}, {
				"auditor0", "Olaci", -1.0, IllegalArgumentException.class
				// Score must be between 0 and 10
			}, {
				"auditor0", "", 5.0, IllegalArgumentException.class
				// Text must not be blank
			}, {
				"auditor0", "HolakTal", 5.0, null
				// Nice testing, no business rule broken here
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.selfAssignAndSaveAuditTemplate((String) testingData[i][0], (String) testingData[i][1], (Double) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	@Test
	public void editAuditDriver() {
		Object testingData[][] = {
			{
				"rookie0", "audit1", "Yeahboy", IllegalArgumentException.class
				// Rookies cannot edit audits
			}, {
				"auditor1", "audit1", "Yeahboy", IllegalArgumentException.class
				// This auditor doesn't own this audit
			}, {
				"auditor0", "audit1", "", IllegalArgumentException.class
				// Text must not be blank
			}, {
				"auditor0", "audit0", "Yeahboy", IllegalArgumentException.class
				// Trying to edit an audit in final mode
			}, {
				"auditor0", "audit1", "Finally fine", null
				// Nice testing, no business rule broken here
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editAuditTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	@Test
	public void deleteAuditDriver() {
		Object testingData[][] = {
			{
				"rookie0", "audit1", IllegalArgumentException.class
				// Rookies cannot delete audits
			}, {
				"auditor1", "audit1", IllegalArgumentException.class
				// This auditor doesn't own this audit
			}, {
				"auditor0", "audit0", IllegalArgumentException.class
				// Trying to delete an audit in final mode
			}, {
				"auditor0", "audit1", null
				// Nice testing, no business rule broken here
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteAuditTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	public void selfAssignAndSaveAuditTemplate(String username, String text, Double score, Class<?> expected) {

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Position pos = this.positionService.findOne(super.getEntityId("position0"));
			Audit audit = this.auditService.selfAssign(pos.getId());
			audit.setText(text);
			audit.setScore(score);
			Audit saved = this.auditService.save(audit);
			Assert.notNull(saved);
			Assert.notNull(saved.getAuditor());
			Assert.isTrue(saved.getId() != 0);
			Assert.isTrue(pos.getAudits().contains(saved));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void editAuditTemplate(String username, String toEdit, String text, Class<?> expected){

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Audit audit = this.auditService.findOneToEdit(super.getEntityId(toEdit));
			audit.setText(text);
			Audit saved = this.auditService.save(audit);
			Assert.notNull(saved);
			Assert.isTrue(saved.getText().equals(text));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

	public void deleteAuditTemplate(String username, String toDelete, Class<?> expected){

		Class<?> caught = null;

		try {
			super.authenticate(username);
			Audit audit = this.auditService.findOneToEdit(super.getEntityId(toDelete));
			this.auditService.delete(audit.getId());
			Assert.isTrue(!this.auditService.exists(audit.getId()));
			Assert.isTrue(!this.positionService.findOne(super.getEntityId("position0")).getAudits().contains(audit));
			super.unauthenticate();
		} catch (Throwable oops) {
			caught = oops.getClass();
		}

		super.checkExceptions(expected, caught);
	}

}
