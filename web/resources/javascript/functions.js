/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var invNum="#{ReportBean.invNum}";

function doPopup(source) {
    invNum=document.getElementById('invoiceForm:invoiceSelector').value;
    invFmt=document.getElementById('invoiceForm:exportOption').value;
    var loc="/Contractor/reports/invpreview.xhtml";
    popup=window.open(loc,"popup","height=800,width=600,toolbar=yes,menubar=no");
    popup.getElementById('popupForm:num').value=invNum;
    popup.focus();
}

function doPreview(invNumber,expFormat) {
    alert(document.forms[0]['popupForm:num'].value);
    window.close();    
}

function popup(source,name) {
    //formId=source.form.id;
    invDate=source.document.getElementById("invoiceForm:invoiceDate").value;
    alert(invDate);
    win=window.open("/reports/InvoiceForm.xhtml", name, "height=800,width=600,toolbar=no,menubar=no");
    win.openerFormId=formId;
    win.focus();
    document.getElementById("invoiceForm:invSelector").value=invDate;
    document.getElementById("invoiceForm:getReport").onClick(null);
}

function showInfo() {
    n=document.getElementById('popupForm:num').value;
    alert(n);
}

function send() {
    alert("Hi");
}