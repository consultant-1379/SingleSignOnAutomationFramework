/*
 * COPYRIGHT Ericsson 2013
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *
 * @author ealemca
 */
package com.ericsson.nms.sso.taf.test.cases;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.VUsers;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.nms.sso.taf.test.operators.SsoOperator;
import com.ericsson.nms.sso.taf.test.operators.SsoRmtCmdOperator;
import com.ericsson.nms.sso.taf.test.operators.SsoServiceGroupOperator;
import com.ericsson.nms.sso.taf.test.operators.SsoadmOperator;
import com.ericsson.nms.sso.taf.test.data.SsoTestDataProvider;
import org.testng.annotations.Test;
import static se.ericsson.jcat.fw.ng.JcatNGTestBase.assertEquals;
import static se.ericsson.jcat.fw.ng.JcatNGTestBase.assertFalse;
import static se.ericsson.jcat.fw.ng.JcatNGTestBase.assertTrue;

/**
*
*	Class to execute tests against SSOBackupRestore
**/
public class SsoBackupRestore extends TorTestCaseHelper implements TestCase{

	SsoRmtCmdOperator remOperator = new SsoRmtCmdOperator();
	SsoOperator ssoOperator = new SsoOperator();
	SsoadmOperator admOper = new SsoadmOperator();
	SsoServiceGroupOperator sgOper = new SsoServiceGroupOperator();
	
	/**
	/* SSO_BackupRestore_verifyBackupCreate
	/* @DESCRIPTION Verify backup script creates a backup
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.API})
	@Test (groups = {"regression"},dataProvider="PeerNodes",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_BackupRestore_verifyBackupCreate(Host host){
		setTestcase("TORFTUISSO-476_Func_1","SSO_BackupRestore_verifyBackupCreate");

		String label = ssoOperator.createLabel();
		setTestStep("Creating backup.sh create <label>");
		
		assertTrue(remOperator.runBackupScript(host, "create", label));

		setTestStep("backup.sh list should contain label :"+label);
		assertTrue(remOperator.runBackupScript(host, "list"));
		
		//Verify last result contains label above
		assertTrue(remOperator.getLastResult().contains(label));
	}


	/**
	/* SSO_BackupRestore_verifyBackupRemove
	/* @DESCRIPTION Verify backup script removes a backup
	/* @PRE create a backup
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.API})
	@Test (groups = {"regression"},dataProvider="PeerNodes",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_BackupRestore_verifyBackupRemove(Host host){
		setTestcase("TORFTUISSO-476_Func_2","SSO_BackupRestore_verifyBackupRemove");
		
		String label = ssoOperator.createLabel();
		
		setTestStep("Creating backup.sh create <label>");
		assertTrue(remOperator.runBackupScript(host, "create", label));
		
		setTestStep("backup.sh list should contain label :"+label);
		assertTrue(remOperator.runBackupScript(host, "list"));
		
		//Verify last result contains label above
		assertTrue(remOperator.getLastResult().contains(label));
		
		setTestStep("backup.sh delete <label>");
		assertTrue(remOperator.runBackupScript(host, "remove", label));


		setTestStep("backup.sh list | grep <label> should result in exit code > 0");
		assertFalse(remOperator.getLastResult().contains(label));

	}


	/**
	/* SSO_BackupRestore_verifyBackupRestore
	/* @DESCRIPTION Verify backup script restores a previous backup
	/* @PRE Create a backup, create some dummy config and take another backup
	/* @PRIORITY MEDIUM
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.API})
	@Test (groups = {"regression"},dataProvider="PeerNodes",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_BackupRestore_verifyBackupRestore(Host host){
		setTestcase("TORFTUISSO-476_Func_3","SSO_BackupRestore_verifyBackupRestore");

        setTestStep("Lock Apache service units");
        assertTrue(sgOper.lockApaches(host));
        
		setTestStep("Change a parameters to test times");
		admOper.setHost(host);
		admOper.setDefaultTimeouts();
		
		setTestStep("Create a backup");
		String label = ssoOperator.createLabel();
		
		setTestStep("Creating backup.sh create <label>");
		assertTrue(remOperator.runBackupScript(host, "create", label));
		
		setTestStep("Change parameter to different value.");
		admOper.setTimeoutsForTest();
		
		setTestStep("backup.sh restore <label>");
		assertTrue(remOperator.runBackupScript(host, "restore", label));
		
//		setTestStep("Now restart OpenAM");
//        System.out.println("Restarting OpenAM");
//		sgOper.lockSsoJboss(host);
//		sleep(30);
//		sgOper.unlockSsoJboss(host);
//		sleep(30);

        setTestStep("Unlock Apache service units");
        assertTrue(sgOper.unlockApaches(host));
		
		setTestStep("ssoadm <show config> should show config has changed.");
		assertEquals(admOper.getInactivityPeriod(), Integer.parseInt(SsoadmOperator.DEFAULTMAXIDLETIME));
		assertEquals(admOper.getTimeoutPeriod(), Integer.parseInt(SsoadmOperator.DEFAULTMAXSESSIONTIME));

		
	}


	/**
	/* SSO_BackupRestore_shouldShowUsage
	/* @DESCRIPTION Verify script produces usage info
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.API})
	@Test (groups = {"regression"},dataProvider="PeerNodes",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_BackupRestore_shouldShowUsage(Host host){
		setTestcase("TORFTUISSO-476_Robu_1","SSO_BackupRestore_shouldShowUsage");

		setTestStep("backup.sh | grep -i usage");
		remOperator.runBackupScript(host);	
		assertTrue(remOperator.getLastResult().contains("Usage"));
	}


	/**
	/* SSO_BackupRestore_MultipleRestores
	/* @DESCRIPTION Verify multiple backups and restores can be performed in a row.
	/* @PRE <enter any setup steps needed before the test case is run>
	/* @PRIORITY LOW
	*/
	@VUsers(vusers = {1})
	@Context(context = {Context.API})
	@Test (groups = {"regression"},dataProvider="PeerNodes",dataProviderClass=SsoTestDataProvider.class)
	public void sSO_BackupRestore_MultipleRestores(Host host){
		setTestcase("TORFTUISSO-476_Robu_2","SSO_BackupRestore_MultipleRestores");
        
        setTestStep("Create a backup");
		String label = ssoOperator.createLabel();
		
		setTestStep("Creating backup.sh create <label>");
		assertTrue(remOperator.runBackupScript(host, "create", label));
		
		setTestStep("Creating a backup with the same label");
		assertTrue(remOperator.runBackupScript(host, "create", label));
        
        //TODO: implement RemoteFilehandler and check for last modified time
	}

}