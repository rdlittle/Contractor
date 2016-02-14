/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webfront.converter;

import com.webfront.beans.ContractorService;
import com.webfront.model.Theme;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author rlittle
 */
@FacesConverter(value="themeConverter")
public class ThemeConverter
  implements Converter
{
  public Object getAsObject(FacesContext fc, UIComponent uic, String value)
  {
    if ((value != null) && (value.trim().length() > 0)) {
      try
      {
        ContractorService service = (ContractorService)fc.getExternalContext().getApplicationMap().get("contractorService");
        return service.getThemes().get(Integer.parseInt(value));
      }
      catch (NumberFormatException e)
      {
        throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid theme."));
      }
    }
    return null;
  }
  
  public String getAsString(FacesContext fc, UIComponent uic, Object object)
  {
    if (object != null) {
      return String.valueOf(((Theme)object).getId());
    }
    return null;
  }
}
