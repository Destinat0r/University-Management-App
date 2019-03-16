package com.foxminded.university.servlets.lecture_hall;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foxminded.university.dao.impl.LectureHallDao;
import com.foxminded.university.domain.LectureHall;

@WebServlet("/lecture-halls")
public class AllLectureHallsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<LectureHall> lectureHalls = new LectureHallDao().findAll();

        request.setAttribute("lectureHalls", lectureHalls);
        request.getRequestDispatcher("jsp/lecture_hall/all_lecture_halls.jsp").forward(request, response);
    }
}
