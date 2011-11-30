/**
 * OW2 FraSCAti Web Explorer
 * Copyright (C) 2011 INRIA, University of Lille 1
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * Contact: frascati@ow2.org
 *
 * Author: Philippe Merle
 *
 * Contributor(s):
 *
 */

package org.easysoa.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.easysoa.api.ServiceManager;
import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Scope;
import org.oasisopen.sca.annotation.Service;

/**
 * OW2 FraSCAti Web Explorer Uploader component implementation.
 *
 * @author <a href="mailto:philippe.merle@inria.fr">Philippe Merle</a>
 * @version 1.5
 */
@Scope("COMPOSITE")
@Service(Servlet.class)
public class UploaderImpl
     extends HttpServlet
{
  //---------------------------------------------------------------------------
  // Internal state.
  // --------------------------------------------------------------------------

  /**
   * Reference to the OW2 FraSCAti composite manager.
   */
  @Reference
  protected ServiceManager serviceManager;

  //---------------------------------------------------------------------------
  // Internal methods.
  // --------------------------------------------------------------------------

  /**
   * @see HttpServlet#service(HttpServletRequest, HttpServletResponse)
   */
  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
	  System.out.println("UPLOAD SERVICE");
    if (ServletFileUpload.isMultipartContent(request)) {
      ServletFileUpload servletFileUpload = new ServletFileUpload(
          new DiskFileItemFactory());
      List<FileItem> fileItemList = null;
      try {
        fileItemList = servletFileUpload.parseRequest(request);
      } catch(FileUploadException fue) {
        throw new ServletException(fue);
      }

      File saveTo = null;
      for(FileItem fileItem : fileItemList) {
    	  System.out.println(fileItem.getFieldName());
        if(fileItem.getFieldName().equals("filename")) {
          if(fileItem.getSize() > 0) {
            saveTo = new File(serviceManager.getCurrentApplication().getSources() + File.separator + "impl" + File.separator +fileItem.getName());
            System.out.println("saveTo : "+saveTo.getPath());
	        try {
	          fileItem.write(saveTo);
            } catch(Exception e) {
              throw new ServletException(e);
            }
          }
        } 
      }

    }
  }

  //---------------------------------------------------------------------------
  // Public methods.
  // --------------------------------------------------------------------------

}
