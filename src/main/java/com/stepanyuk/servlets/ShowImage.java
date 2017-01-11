package com.stepanyuk.servlets;

import com.stepanyuk.controllers.BookListController;
import com.stepanyuk.entity.BookEntity;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class ShowImage extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("image/jpeg");

        OutputStream out = response.getOutputStream();
        try {
            int index = Integer.valueOf(request.getParameter("index"));
            BookListController bookListController = (BookListController) request.getSession(false).getAttribute("bookListController");
            byte[] image = ((BookEntity)bookListController.getPager().getList().get(index)).getImage();
            response.setContentLength(image.length);
            out.write(image);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            out.close();
        }

//        OutputStream out = response.getOutputStream();
//        try {
////            int index = Integer.valueOf(request.getParameter("id_idx"));
//            Long index = Long.valueOf(request.getParameter("id_idx"));
//
//            BookListController searchController = (BookListController)request.getSession(false).getAttribute("bookListController");
//
////            Book book = ((List<Book>)request.getSession(false).getAttribute("curBookList")).get(index);
//            byte[] image = searchController.getImage(index);
//            response.setContentLength(image.length);
//            out.write(image);
//        } finally {
//            out.close();
//        }
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

