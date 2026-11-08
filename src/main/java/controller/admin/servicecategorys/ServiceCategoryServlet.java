package controller.admin.servicecategorys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ServiceCategory;
import service.ServiceCategoryService;
import service.impl.ServiceCategoryServiceImpl;

@WebServlet("/admin/service-categories")
public class ServiceCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int PAGE_SIZE = 5;
    private static final int MAX_KEYWORD_LENGTH = 100;

    private ServiceCategoryService service;

    @Override
    public void init() throws ServletException {
        service = new ServiceCategoryServiceImpl();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String keyword =
                normalize(request.getParameter("keyword"));

        String status =
                normalize(request.getParameter("status"))
                        .toUpperCase(Locale.ROOT);

        String sort =
                normalize(request.getParameter("sort"));

        int currentPage =
                parsePositiveInteger(
                        request.getParameter("page"),
                        1);

        String warningMessage = null;

        if (keyword.length() > MAX_KEYWORD_LENGTH) {

            keyword = keyword.substring(
                    0,
                    MAX_KEYWORD_LENGTH);

            warningMessage =
                    "Từ khóa tìm kiếm tối đa 100 ký tự.";
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

            List<ServiceCategory> allCategories =
                    service.getAllCategories();

            if (allCategories == null) {
                allCategories = Collections.emptyList();
            }

            /*
             * Search + Filter
             */
            List<ServiceCategory> filteredCategories =
                    new ArrayList<>();

            for (ServiceCategory category : allCategories) {

                if (category == null
                        || category.getCategoryId() <= 0) {

                    continue;
                }

                if (!matchesKeyword(category, keyword)) {
                    continue;
                }

                if (!matchesStatus(category, status)) {
                    continue;
                }

                filteredCategories.add(category);
            }

            /*
             * Sort
             */
            sortCategories(filteredCategories, sort);

            /*
             * Pagination
             */
            int totalItems = filteredCategories.size();

            int totalPages =
                    totalItems == 0
                            ? 1
                            : (int) Math.ceil(
                                    (double) totalItems / PAGE_SIZE);

            if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            int fromIndex =
                    (currentPage - 1) * PAGE_SIZE;

            int toIndex =
                    Math.min(
                            fromIndex + PAGE_SIZE,
                            totalItems);

            List<ServiceCategory> pageCategories;

            if (totalItems == 0
                    || fromIndex >= totalItems) {

                pageCategories =
                        Collections.emptyList();

            } else {

                pageCategories =
                        new ArrayList<>(
                                filteredCategories.subList(
                                        fromIndex,
                                        toIndex));
            }

            request.setAttribute(
                    "categories",
                    pageCategories);

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
                    "page",
                    currentPage);

            request.setAttribute(
                    "currentPage",
                    currentPage);

            request.setAttribute(
                    "totalPages",
                    totalPages);

            request.setAttribute(
                    "totalItems",
                    totalItems);

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
                    "Không thể tải danh sách nhóm dịch vụ.");

            request.setAttribute(
                    "categories",
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

            request.setAttribute("page", 1);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.setAttribute("totalItems", 0);
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/service-categories/list.jsp")
                .forward(request, response);
    }

    private boolean matchesKeyword(
            ServiceCategory category,
            String keyword) {

        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String categoryName =
                normalize(category.getCategoryName())
                        .toLowerCase(Locale.ROOT);

        String normalizedKeyword =
                keyword.toLowerCase(Locale.ROOT);

        return categoryName.contains(normalizedKeyword);
    }

    private boolean matchesStatus(
            ServiceCategory category,
            String selectedStatus) {

        if (selectedStatus == null
                || selectedStatus.isBlank()) {

            return true;
        }

        return category.getStatus() != null
                && category.getStatus()
                        .trim()
                        .equalsIgnoreCase(selectedStatus);
    }

    private void sortCategories(
            List<ServiceCategory> categories,
            String sort) {

        if (categories == null
                || categories.size() < 2) {

            return;
        }

        Comparator<ServiceCategory> comparator;

        switch (sort) {

            case "idAsc":

                comparator =
                        Comparator.comparingInt(
                                ServiceCategory::getCategoryId);

                break;

            case "idDesc":

                comparator =
                        Comparator.comparingInt(
                                ServiceCategory::getCategoryId)
                                .reversed();

                break;

            case "nameAsc":

                comparator =
                        Comparator.comparing(
                                (ServiceCategory category) ->
                                        safeString(
                                                category.getCategoryName()),
                                String.CASE_INSENSITIVE_ORDER);

                break;

            case "nameDesc":

                comparator =
                        Comparator.comparing(
                                (ServiceCategory category) ->
                                        safeString(
                                                category.getCategoryName()),
                                String.CASE_INSENSITIVE_ORDER)
                                .reversed();

                break;

            case "createdAsc":

                comparator =
                        Comparator.comparing(
                                ServiceCategory::getCreatedAt,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()));

                break;

            case "createdDesc":

                comparator =
                        Comparator.comparing(
                                ServiceCategory::getCreatedAt,
                                Comparator.nullsLast(
                                        Comparator.naturalOrder()))
                                .reversed();

                break;

            default:

                comparator =
                        Comparator.comparingInt(
                                ServiceCategory::getCategoryId)
                                .reversed();

                break;
        }

        categories.sort(comparator);
    }

    private boolean isValidStatus(String status) {

        return status == null
                || status.isBlank()
                || "ACTIVE".equals(status)
                || "INACTIVE".equals(status);
    }

    private boolean isValidSort(String sort) {

        return sort == null
                || sort.isBlank()
                || "idAsc".equals(sort)
                || "idDesc".equals(sort)
                || "nameAsc".equals(sort)
                || "nameDesc".equals(sort)
                || "createdAsc".equals(sort)
                || "createdDesc".equals(sort);
    }

    private int parsePositiveInteger(
            String value,
            int defaultValue) {

        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {

            int result =
                    Integer.parseInt(value.trim());

            return result > 0
                    ? result
                    : defaultValue;

        } catch (NumberFormatException exception) {

            return defaultValue;
        }
    }

    private String safeString(String value) {

        return value == null
                ? ""
                : value.trim();
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value.trim();
    }
}