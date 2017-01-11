package com.stepanyuk.servlets;

import com.stepanyuk.controllers.BookListController;
import com.stepanyuk.db.DataHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@WebServlet(name = "PdfContent", urlPatterns = {"/PdfContent"})
public class PdfContent extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/pdf");
        OutputStream out = response.getOutputStream();
        try {
            long id = Long.valueOf(request.getParameter("id"));
//            Boolean save = Boolean.valueOf(request.getParameter("save"));
            String filename = request.getParameter("filename");

            BookListController searchController = (BookListController) request.getSession(false).getAttribute("bookListController");
//            byte[] content = searchController.getContent(id);
            byte[] content = searchController.getDataHelper().getContent(id);
            response.setContentLength(content.length);
            if (request.getParameter("operation").trim().equals("save")) {
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(request.getParameter("name").trim(), "UTF-8")  + ".pdf");
            }else {

            }
            out.write(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
