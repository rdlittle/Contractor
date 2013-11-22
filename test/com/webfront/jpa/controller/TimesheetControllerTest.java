/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.jpa.controller;

import com.webfront.entity.Timesheet;
import com.webfront.jpa.controller.util.PaginationHelper;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author rlittle
 */
public class TimesheetControllerTest {
    
    public TimesheetControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getSelected method, of class TimesheetController.
     */
    @Test
    public void testGetSelected() {
        System.out.println("getSelected");
        TimesheetController instance = new TimesheetController();
        Timesheet expResult = null;
        Timesheet result = instance.getSelected();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPagination method, of class TimesheetController.
     */
    @Test
    public void testGetPagination() {
        System.out.println("getPagination");
        TimesheetController instance = new TimesheetController();
        PaginationHelper expResult = null;
        PaginationHelper result = instance.getPagination();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prepareList method, of class TimesheetController.
     */
    @Test
    public void testPrepareList() {
        System.out.println("prepareList");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.prepareList();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prepareView method, of class TimesheetController.
     */
    @Test
    public void testPrepareView() {
        System.out.println("prepareView");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.prepareView();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prepareCreate method, of class TimesheetController.
     */
    @Test
    public void testPrepareCreate() {
        System.out.println("prepareCreate");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.prepareCreate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class TimesheetController.
     */
    @Test
    public void testCreate() {
        System.out.println("create");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.create();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of prepareEdit method, of class TimesheetController.
     */
    @Test
    public void testPrepareEdit() {
        System.out.println("prepareEdit");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.prepareEdit();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class TimesheetController.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.update();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroy method, of class TimesheetController.
     */
    @Test
    public void testDestroy() {
        System.out.println("destroy");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.destroy();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of destroyAndView method, of class TimesheetController.
     */
    @Test
    public void testDestroyAndView() {
        System.out.println("destroyAndView");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.destroyAndView();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItems method, of class TimesheetController.
     */
    @Test
    public void testGetItems() {
        System.out.println("getItems");
        TimesheetController instance = new TimesheetController();
        DataModel expResult = null;
        DataModel result = instance.getItems();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of next method, of class TimesheetController.
     */
    @Test
    public void testNext() {
        System.out.println("next");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.next();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of previous method, of class TimesheetController.
     */
    @Test
    public void testPrevious() {
        System.out.println("previous");
        TimesheetController instance = new TimesheetController();
        String expResult = "";
        String result = instance.previous();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItemsAvailableSelectMany method, of class TimesheetController.
     */
    @Test
    public void testGetItemsAvailableSelectMany() {
        System.out.println("getItemsAvailableSelectMany");
        TimesheetController instance = new TimesheetController();
        SelectItem[] expResult = null;
        SelectItem[] result = instance.getItemsAvailableSelectMany();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getItemsAvailableSelectOne method, of class TimesheetController.
     */
    @Test
    public void testGetItemsAvailableSelectOne() {
        System.out.println("getItemsAvailableSelectOne");
        TimesheetController instance = new TimesheetController();
        SelectItem[] expResult = null;
        SelectItem[] result = instance.getItemsAvailableSelectOne();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
