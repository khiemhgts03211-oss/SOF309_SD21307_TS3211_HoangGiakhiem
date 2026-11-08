package controller.admin.showtimes;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Showtime;
import service.ShowtimeService;
import service.impl.ShowtimeServiceImpl;

@WebServlet("/admin/showtimes")
public class ShowtimeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 5;
    private static final int MAX_KEYWORD_LENGTH = 150;

    private static final Pattern DIACRITICS =
            Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private ShowtimeService showtimeService;

    @Override
    public void init() throws ServletException {

        try {

            showtimeService =
                    new ShowtimeServiceImpl();

        } catch (Exception exception) {

            throw new ServletException(
                    "Không thể khởi tạo ShowtimeService.",
                    exception);
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        prepareResponse(request, response);

        String keyword =
                removeControlCharacters(
                        request.getParameter("keyword"));

        String status =
                normalizeUpper(
                        request.getParameter("status"));

        String sort =
                normalize(
                        request.getParameter("sort"))
                        .toLowerCase(Locale.ROOT);

        int currentPage =
                parsePositiveInteger(
                        request.getParameter("page"),
                        1);

        String warningMessage = null;

        /*
         * =============================================
         * KIỂM TRA THAM SỐ
         * =============================================
         */

        if (keyword.length() > MAX_KEYWORD_LENGTH) {

            keyword =
                    keyword.substring(
                            0,
                            MAX_KEYWORD_LENGTH);

            warningMessage =
                    "Từ khóa tìm kiếm tối đa 150 ký tự.";
        }

        if (!isValidStatus(status)) {

            status = "";

            warningMessage =
                    "Trạng thái được chọn không hợp lệ.";
        }

        if (!isValidSort(sort)) {
            sort = "";
        }

        try {

            /*
             * =============================================
             * LẤY TOÀN BỘ SUẤT CHIẾU
             * =============================================
             */

            List<Showtime> allShowtimes =
                    showtimeService.getAllShowtimes();

            if (allShowtimes == null) {
                allShowtimes = Collections.emptyList();
            }

            /*
             * =============================================
             * SEARCH + FILTER
             * =============================================
             */

            List<Showtime> filteredShowtimes =
                    new ArrayList<>();

            for (Showtime showtime : allShowtimes) {

                if (!isValidBasicShowtime(showtime)) {
                    continue;
                }

                if (!matchesKeyword(
                        showtime,
                        keyword)) {

                    continue;
                }

                if (!matchesStatus(
                        showtime,
                        status)) {

                    continue;
                }

                filteredShowtimes.add(showtime);
            }

            /*
             * =============================================
             * SORT
             * =============================================
             */

            sortShowtimes(
                    filteredShowtimes,
                    sort);

            /*
             * =============================================
             * PHÂN TRANG
             * =============================================
             */

            int totalItems =
                    filteredShowtimes.size();

            int totalPages =
                    totalItems == 0
                            ? 1
                            : (int) Math.ceil(
                                    (double) totalItems
                                            / PAGE_SIZE);

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int fromIndex =
                    (currentPage - 1) * PAGE_SIZE;

            int toIndex =
                    Math.min(
                            fromIndex + PAGE_SIZE,
                            totalItems);

            List<Showtime> pageShowtimes;

            if (totalItems == 0
                    || fromIndex >= totalItems) {

                pageShowtimes =
                        Collections.emptyList();

            } else {

                pageShowtimes =
                        new ArrayList<>(
                                filteredShowtimes.subList(
                                        fromIndex,
                                        toIndex));
            }

            /*
             * =============================================
             * ATTRIBUTE CHO JSP
             * =============================================
             */

            request.setAttribute(
                    "showtimes",
                    pageShowtimes);

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "status",
                    status);

            request.setAttribute(
                    "selectedStatus",
                    status);

            request.setAttribute(
                    "sort",
                    sort);

            request.setAttribute(
                    "currentPage",
                    currentPage);

            /*
             * Giữ tương thích nếu JSP cũ dùng ${page}.
             */
            request.setAttribute(
                    "page",
                    currentPage);

            request.setAttribute(
                    "totalPages",
                    totalPages);

            request.setAttribute(
                    "totalItems",
                    totalItems);

            request.setAttribute(
                    "pageSize",
                    PAGE_SIZE);

            request.setAttribute(
                    "hasCriteria",
                    !keyword.isBlank()
                            || !status.isBlank()
                            || !sort.isBlank());

            if (warningMessage != null) {

                request.setAttribute(
                        "warningMessage",
                        warningMessage);
            }

        } catch (Exception exception) {

            exception.printStackTrace();

            request.setAttribute(
                    "errorMessage",
                    "Không thể tải danh sách suất chiếu. "
                            + "Vui lòng thử lại sau.");

            request.setAttribute(
                    "showtimes",
                    Collections.emptyList());

            request.setAttribute(
                    "keyword",
                    keyword);

            request.setAttribute(
                    "status",
                    status);

            request.setAttribute(
                    "selectedStatus",
                    status);

            request.setAttribute(
                    "sort",
                    sort);

            request.setAttribute(
                    "currentPage",
                    1);

            request.setAttribute(
                    "page",
                    1);

            request.setAttribute(
                    "totalPages",
                    1);

            request.setAttribute(
                    "totalItems",
                    0);

            request.setAttribute(
                    "pageSize",
                    PAGE_SIZE);

            request.setAttribute(
                    "hasCriteria",
                    !keyword.isBlank()
                            || !status.isBlank()
                            || !sort.isBlank());
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/showtimes/list.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Trang danh sách suất chiếu chỉ hỗ trợ GET.");
    }

    /*
     * =============================================
     * TÌM KIẾM
     * =============================================
     */

    private boolean matchesKeyword(
            Showtime showtime,
            String keyword) {

        if (keyword == null
                || keyword.isBlank()) {

            return true;
        }

        String normalizedKeyword =
                normalizeForSearch(keyword);

        String movieTitle =
                normalizeForSearch(
                        showtime.getMovieTitle());

        String roomName =
                normalizeForSearch(
                        showtime.getRoomName());

        String showtimeStatus =
                normalizeForSearch(
                        showtime.getStatus());

        return movieTitle.contains(normalizedKeyword)
                || roomName.contains(normalizedKeyword)
                || showtimeStatus.contains(normalizedKeyword);
    }

    /*
     * =============================================
     * LỌC TRẠNG THÁI
     * =============================================
     */

    private boolean matchesStatus(
            Showtime showtime,
            String selectedStatus) {

        if (selectedStatus == null
                || selectedStatus.isBlank()) {

            return true;
        }

        return showtime.getStatus() != null
                && showtime.getStatus()
                        .trim()
                        .equalsIgnoreCase(
                                selectedStatus.trim());
    }

    /*
     * =============================================
     * SẮP XẾP
     * =============================================
     */

    private void sortShowtimes(
            List<Showtime> showtimes,
            String sort) {

        if (showtimes == null
                || showtimes.size() < 2) {

            return;
        }

        Comparator<Showtime> comparator;

        switch (sort) {

            case "date_asc":

                comparator =
                        Comparator.comparing(
                                Showtime::getShowDate,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .thenComparing(
                                        Showtime::getStartTime,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()));

                break;

            case "date_desc":

                comparator =
                        Comparator.comparing(
                                Showtime::getShowDate,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .thenComparing(
                                        Showtime::getStartTime,
                                        Comparator.nullsLast(
                                                Comparator.naturalOrder()))
                                .reversed();

                break;

            case "price_asc":

                comparator =
                        Comparator.comparing(
                                Showtime::getTicketPrice,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()));

                break;

            case "price_desc":

                comparator =
                        Comparator.comparing(
                                Showtime::getTicketPrice,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .reversed();

                break;

            case "movie_asc":

                comparator =
                        Comparator.comparing(
                                (Showtime showtime) ->
                                        safeString(
                                                showtime.getMovieTitle()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "movie_desc":

                comparator =
                        Comparator.comparing(
                                (Showtime showtime) ->
                                        safeString(
                                                showtime.getMovieTitle()),
                                String.CASE_INSENSITIVE_ORDER)
                                .reversed();

                break;

            case "room_asc":

                comparator =
                        Comparator.comparing(
                                (Showtime showtime) ->
                                        safeString(
                                                showtime.getRoomName()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "room_desc":

                comparator =
                        Comparator.comparing(
                                (Showtime showtime) ->
                                        safeString(
                                                showtime.getRoomName()),
                                String.CASE_INSENSITIVE_ORDER)
                                .reversed();

                break;

            default:

                comparator =
                        Comparator.comparingInt(
                                Showtime::getShowtimeId)
                                .reversed();

                break;
        }

        showtimes.sort(comparator);
    }

    /*
     * =============================================
     * VALIDATION
     * =============================================
     */

    private boolean isValidBasicShowtime(
            Showtime showtime) {

        return showtime != null
                && showtime.getShowtimeId() > 0;
    }

    private boolean isValidStatus(
            String status) {

        return status == null
                || status.isBlank()
                || "OPEN".equals(status)
                || "CLOSED".equals(status)
                || "CANCELLED".equals(status);
    }

    private boolean isValidSort(
            String sort) {

        return sort == null
                || sort.isBlank()
                || "date_asc".equals(sort)
                || "date_desc".equals(sort)
                || "price_asc".equals(sort)
                || "price_desc".equals(sort)
                || "movie_asc".equals(sort)
                || "movie_desc".equals(sort)
                || "room_asc".equals(sort)
                || "room_desc".equals(sort);
    }

    /*
     * =============================================
     * PARSE PAGE
     * =============================================
     */

    private int parsePositiveInteger(
            String value,
            int defaultValue) {

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {

            int parsedValue =
                    Integer.parseInt(
                            value.trim());

            return parsedValue > 0
                    ? parsedValue
                    : defaultValue;

        } catch (NumberFormatException exception) {

            return defaultValue;
        }
    }

    /*
     * =============================================
     * NORMALIZATION
     * =============================================
     */

    private String normalizeForSearch(
            String value) {

        String normalizedValue =
                normalize(value)
                        .toLowerCase(Locale.ROOT);

        normalizedValue =
                Normalizer.normalize(
                        normalizedValue,
                        Normalizer.Form.NFD);

        normalizedValue =
                DIACRITICS.matcher(
                        normalizedValue)
                        .replaceAll("");

        /*
         * NFD không chuyển chữ đ thành d.
         */
        return normalizedValue
                .replace('đ', 'd');
    }

    private String removeControlCharacters(
            String value) {

        if (value == null || value.isBlank()) {
            return "";
        }

        StringBuilder result =
                new StringBuilder();

        for (int index = 0;
                index < value.length();
                index++) {

            char character =
                    value.charAt(index);

            if (!Character.isISOControl(character)) {
                result.append(character);
            }
        }

        return result.toString().trim();
    }

    private String normalizeUpper(
            String value) {

        return normalize(value)
                .toUpperCase(Locale.ROOT);
    }

    private String normalize(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String safeString(
            String value) {

        return value == null
                ? ""
                : value.trim();
    }

    /*
     * =============================================
     * RESPONSE
     * =============================================
     */

    private void prepareResponse(
            HttpServletRequest request,
            HttpServletResponse response)
            throws java.io.UnsupportedEncodingException {

        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");

        response.setContentType(
                "text/html; charset=UTF-8");

        response.setHeader(
                "Cache-Control",
                "no-cache, no-store, must-revalidate");

        response.setHeader(
                "Pragma",
                "no-cache");

        response.setDateHeader(
                "Expires",
                0);
    }
}