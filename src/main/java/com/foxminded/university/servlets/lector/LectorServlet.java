package com.foxminded.university.servlets.lector;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foxminded.university.dao.LectorDao;
import com.foxminded.university.dao.SubjectDao;
import com.foxminded.university.dao.impl.LectorDaoImpl;
import com.foxminded.university.dao.impl.SubjectDaoImpl;
import com.foxminded.university.domain.Lector;
import com.foxminded.university.domain.Subject;

@WebServlet("/lector")
public class LectorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private LectorDao lectorDao;
    private SubjectDao subjectDao;
    
    @Override
    public void init() throws ServletException {
        lectorDao = new LectorDaoImpl();
        subjectDao = new SubjectDaoImpl();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int lectorId = Integer.parseInt(request.getParameter("id"));
        Lector lector = lectorDao.findById(lectorId);
        
        List<Subject> subjects = subjectDao.findAllByLectorId(lectorId);
        
        request.setAttribute("lector", lector);
        request.setAttribute("subjects", subjects);
        
        request.getRequestDispatcher("jsp/lector/lector.jsp").forward(request, response);
    }
}
