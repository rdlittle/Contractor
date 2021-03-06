/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.jpa.controller;

import com.webfront.beans.InvoiceFacade;
import com.webfront.beans.PeriodsFacade;
import com.webfront.beans.TimesheetFacade;
import com.webfront.entity.Invoice;
import com.webfront.entity.Periods;
import com.webfront.entity.Timesheet;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author rlittle
 */
@ManagedBean(name="billingController")
@RequestScoped
public class BillingController {
    
    @EJB
    private TimesheetFacade timesheetFacade;
    @EJB
    private PeriodsFacade periodsFacade;
    @EJB
    private InvoiceFacade invoiceFacade;
    
    
    public BillingController() {
        
    }
    
    public String commit(ArrayList<Timesheet> list) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext et = fc.getExternalContext();
            Map<String, String> requestMap;
            requestMap = et.getRequestParameterMap();
            
            String invoiceNum = getInvoiceFacade().getNextInv();
            String invoiceDate = requestMap.get("invoiceForm:invDate_input");
            String amount = requestMap.get("invoiceForm:invAmount");
            Periods period = getPeriodsFacade().getNextPeriod();
            
            Calendar cal = Calendar.getInstance(Locale.getDefault());
            cal.setTime(period.getEndDate());
            boolean isMonday = false;
            
            amount = amount.replaceAll(",", "");
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
            Date d = df.parse(invoiceDate);
            
            Integer clientId=0;
            for(Timesheet t : list) {
                t.setInvNum(invoiceNum);
                t.setPosted(true);
                getTimesheetFacade().edit(t);
                clientId = t.getClientID();
            }
            
            Invoice invoice = new Invoice();
            invoice.setInvoice(invoiceNum);
            invoice.setInvDate(d);
            invoice.setAmount(Float.parseFloat(amount));
            invoice.setClient(clientId);
            invoice.setPeriodNum(period.getId().toString());
            
            getInvoiceFacade().create(invoice);
            getInvoiceFacade().setNextInv(Integer.parseInt(invoiceNum)+1);
            int dow = cal.get(Calendar.DAY_OF_WEEK);
            while(dow != 2) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                dow = cal.get(Calendar.DAY_OF_WEEK);
            }
            
            period = new Periods();
            period.setStartDate(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, 4);
            period.setEndDate(cal.getTime());
            getPeriodsFacade().create(period);
            
            Double serial=Math.random();
            
            return "/invoices/List?faces-redirect=true"+"&serial="+serial.toString();
            
        } catch (ParseException ex) {
            Logger.getLogger(BillingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * @return the timesheetFacade
     */
    public TimesheetFacade getTimesheetFacade() {
        return timesheetFacade;
    }

    /**
     * @param timesheetFacade the timesheetFacade to set
     */
    public void setTimesheetFacade(TimesheetFacade timesheetFacade) {
        this.timesheetFacade = timesheetFacade;
    }

    /**
     * @return the periodsFacade
     */
    public PeriodsFacade getPeriodsFacade() {
        return periodsFacade;
    }

    /**
     * @param periodsFacade the periodsFacade to set
     */
    public void setPeriodsFacade(PeriodsFacade periodsFacade) {
        this.periodsFacade = periodsFacade;
    }

    /**
     * @return the invoiceController
     */
    public InvoiceFacade getInvoiceFacade() {
        return invoiceFacade;
    }

    /**
     * @param facade the InvoiceFacade to set
     */
    public void setInvoiceFacade(InvoiceFacade facade) {
        this.invoiceFacade = facade;
    }
    
    public String getClientName(Integer cid) {
        return getTimesheetFacade().getClientName(cid);
    }
}
