package com.foxminded.university.dao.impl;

import java.sql.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.foxminded.university.dao.ConnectionFactory;
import com.foxminded.university.dao.DaoException;
import com.foxminded.university.dao.LectureDao;
import com.foxminded.university.domain.*;
import com.foxminded.university.utils.Period;

public class LectureDaoImpl implements LectureDao {
    
    private static final String CREATE_QUERY = "INSERT INTO lectures (subject_id, lector_id, group_id, lecture_hall_id, "
            + "period_id) VALUES (?, ?, ?, ?, ?)";
    
    private static final String READ_QUERY = "SELECT subject_id, group_id, lector_id, lecture_hall_id, period_id "
            + "FROM lectures WHERE id = ?";

    private static final String UPDATE_QUERY = "UPDATE lectures SET period_id = ?, subject_id = ?, lector_id = ?, "
            + "group_id = ?, lecture_hall_id = ? "
            + "WHERE id = ?";

    private static final String READ_ALL_QUERY = "SELECT id, subject_id, group_id, lector_id, lecture_hall_id, period_id "
            + "FROM lectures ORDER BY id";

    private static final String DELETE_QUERY = "DELETE FROM lectures WHERE id = ?";
    
    private static final Logger log = LogManager.getLogger(LectureDaoImpl.class.getName());
    
    public Lecture create(Lecture lecture) {

        log.debug("Creating lecture");

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(CREATE_QUERY,
                        Statement.RETURN_GENERATED_KEYS)) {

            log.debug("Prepared statement was created. Setting parameters");

            int subjectId = lecture.getSubject().getId();
            log.trace("Setting subject's id: {}", subjectId);
            statement.setInt(1, subjectId);

            int groupId = lecture.getGroup().getId();
            log.trace("Setting group's id: {}", groupId);
            statement.setInt(2, groupId);

            int lectorId = lecture.getLector().getId();
            log.trace("Setting lector's id: {}", lectorId);
            statement.setInt(3, lectorId);

            int lectureHallId = lecture.getLectureHall().getId();
            log.trace("Setting lecture hall's id: {}", lectureHallId);
            statement.setInt(4, lectureHallId);

            int periodId = lecture.getPeriod().getId();
            log.trace("Setting period's id: {}", periodId);
            statement.setInt(5, periodId);

            log.debug("Executing prepared statement");
            statement.executeUpdate();

            log.debug("Creating result set");

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                log.debug("Result set was created. Setting id from DB to lecture object to return");
                resultSet.next();
                lecture.setId(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            log.error("Creation of lecture has failed.", e);
            throw new DaoException("Creation of lecture has failed.", e);
        }

        log.debug("Lecture {} was created.", lecture);

        return lecture;
    }

    public Lecture findById(int id) {

        log.debug("Finding lecture by id: {}", id);

        Lecture lecture = null;

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {

            log.debug("Prepared statement was created. Setting id");
            statement.setInt(1, id);

            log.debug("Creating result set");
            try (ResultSet resultSet = statement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new SQLException("No such lecture id: " + id);
                }

                log.debug("Getting ids of group, lector and lecure hall from result set");

                log.trace("Getting group's id");
                int groupId = resultSet.getInt("group_id");

                log.trace("Getting lector's id");
                int lectorId = resultSet.getInt("lector_id");

                log.trace("Getting lecture hall's id");
                int lectureHallId = resultSet.getInt("lecture_hall_id");

                log.debug("Parameters have been gotten. Making objects for lecture fields");

                log.trace("Creating PeriodDao object");
                PeriodDaoImpl periodDao = new PeriodDaoImpl();

                log.trace("Creating SubjectDaoImpl object");
                SubjectDaoImpl subjectDao = new SubjectDaoImpl();

                log.debug("Finding the period");
                Period period = periodDao.findById(resultSet.getInt("period_id"));

                log.debug("Finding the subject");
                Subject subject = subjectDao.findById(resultSet.getInt("subject_id"));

                log.debug("Finding the group");
                Group group = new GroupDaoImpl().findById(groupId);

                log.debug("Finding and setting students of the group");
                group.setStudents(new StudentDaoImpl().findAllByGroupId(groupId));

                log.debug("Finding the lector");
                Lector lector = new LectorDaoImpl().findById(lectorId);

                log.debug("Setting subjects of the lector");
                lector.setSubjects(subjectDao.findAllByLectorId(lectorId));

                log.debug("Finding the lecture hall");
                LectureHall lectureHall = new LectureHallDaoImpl().findById(lectureHallId);

                log.debug("Setting booked period of the lecture hall");
                lectureHall.setBookedPeriods(periodDao.findAllByLectureHallId(lectureHallId));

                log.debug("Creating lecture object");
                lecture = new Lecture(id, period, subject, lector, group, lectureHall);
            }
        } catch (SQLException e) {
            log.error("Finding lecture has failed", e);
            throw new DaoException("Finding lecture has failed", e);
        }

        log.debug("Lecture with id: {} was found", id);

        return lecture;
    }

    public Lecture update(Lecture lecture) {

        log.debug("Updating lecture with id: {}", lecture.getId());

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {

            log.debug("Prepared statement was created. Setting parameters");

            int periodId = lecture.getPeriod().getId();
            log.trace("Setting period's id: {}", periodId);
            statement.setInt(1, periodId);

            int subjectId = lecture.getSubject().getId();
            log.trace("Setting subject's id: {}", subjectId);
            statement.setInt(2, subjectId);

            int lectorId = lecture.getLector().getId();
            log.trace("Setting lector's id: {}", lectorId);
            statement.setInt(3, lectorId);

            int groupId = lecture.getGroup().getId();
            log.trace("Setting group's id: {}", groupId);
            statement.setInt(4, groupId);

            int lectureHallId = lecture.getLectureHall().getId();
            log.trace("Setting lecture hall's id: {}", lectureHallId);
            statement.setInt(5, lectureHallId);

            int lectureId = lecture.getId();
            log.trace("Setting lecture's id: {}", lectureId);
            statement.setInt(6, lectureId);

            log.debug("Executing prepated statement");
            statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Lecture update has failed", e);
            throw new DaoException("Lecture update has failed", e);
        }

        log.debug("Lecture with id: {} was updated.", lecture.getId());

        return lecture;
    }

    public List<Lecture> findAll() {

        log.debug("Finding all lectures");

        List<Lecture> lectures = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(READ_ALL_QUERY)) {

            log.debug("Prepared statement was created. Creating result set");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    log.trace("Getting lecture fields from result set");

                    log.trace("Getting id");
                    int id = resultSet.getInt("id");

                    log.trace("Creating PeriodDao object");
                    PeriodDaoImpl periodDao = new PeriodDaoImpl();

                    log.trace("Finding the period");
                    Period period = periodDao.findById(resultSet.getInt("period_id"));

                    log.trace("Creating SubjectDaoImpl object");
                    SubjectDaoImpl subjectDao = new SubjectDaoImpl();

                    log.trace("Finding the subject");
                    Subject subject = subjectDao.findById(resultSet.getInt("subject_id"));

                    log.trace("Finding the lector");
                    Lector lector = new LectorDaoImpl().findById(resultSet.getInt("lector_id"));

                    log.trace("Setting subjects of the lector");
                    lector.setSubjects(subjectDao.findAllByLectorId(resultSet.getInt("lector_id")));

                    log.trace("Finding the group");
                    Group group = new GroupDaoImpl().findById(resultSet.getInt("group_id"));

                    log.trace("Setting students of the group");
                    group.setStudents(new StudentDaoImpl().findAllByGroupId(resultSet.getInt("group_id")));

                    log.trace("Finding the lecture hall");
                    LectureHall lectureHall = new LectureHallDaoImpl().findById(resultSet.getInt("lecture_hall_id"));

                    log.trace("Setting booked periods of the lecture hall");
                    lectureHall.setBookedPeriods(periodDao.findAllByLectureHallId(resultSet.getInt("lecture_hall_id")));

                    log.trace("Creating the lecture object");
                    Lecture lecture = new Lecture(id, period, subject, lector, group, lectureHall);

                    lectures.add(lecture);
                    log.trace("Lecture was found and added to the list");
                }
            }
        } catch (SQLException e) {
            log.error("Finding all lectures has failed", e);
            throw new DaoException("Finding all lectures has failed", e);
        }

        log.debug("All {} lectures have been found.", lectures.size());

        return lectures;
    }

    public void deleteById(int id) {
        log.debug("Deleting lecture by id: {}", id);

        int affectedRows = 0;
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            log.debug("Prepared statement created. Setting id: {}", id);
            statement.setInt(1, id);

            log.debug("Executing statement");
            affectedRows = statement.executeUpdate();

        } catch (SQLException e) {
            log.error("Deletion has failed", e);
            throw new DaoException("Deletion has failed", e);
        }

        if (affectedRows == 0) {
            log.debug("No lecture with such id: {} in database.", id);
        } else {
            log.debug("Lecture with id: {} was successfully deleted.", id);
        }
    }
}
