package controller.admin.servicecategorys;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.*;
import model.ServiceCategory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.ArgumentCaptor;
import service.impl.ServiceCategoryServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Timeout(10)
class MovieCategoryControllerTest {
    final HttpServletRequest request = mock(HttpServletRequest.class);
    final HttpServletResponse response = mock(HttpServletResponse.class);
    final HttpSession session = mock(HttpSession.class);
    final RequestDispatcher dispatcher = mock(RequestDispatcher.class);
    static final String LIST = "/MovieTicketManagement/admin/service-categories";

    @BeforeEach void configureRequest() {
        when(request.getContextPath()).thenReturn("/MovieTicketManagement");
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getParameter("id")).thenReturn("7");
        when(request.getParameter("categoryId")).thenReturn("7");
        when(request.getParameter("categoryName")).thenReturn("Đồ uống");
        when(request.getParameter("status")).thenReturn("ACTIVE");
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void createOutcome(boolean success) throws Exception {
        try (var services = mockConstruction(ServiceCategoryServiceImpl.class, (service, context) ->
                when(service.createCategory(any(ServiceCategory.class))).thenReturn(success))) {
            new AddServiceCategoryServlet().doPost(request, response);
            var input = ArgumentCaptor.forClass(ServiceCategory.class);
            verify(services.constructed().get(0)).createCategory(input.capture());
            assertEquals("Đồ uống", input.getValue().getCategoryName());
            assertEquals("ACTIVE", input.getValue().getStatus());
            if (success) {
                verify(response).sendRedirect(LIST);
            } else {
                verify(request).setAttribute("error", "Category already exists.");
                verify(dispatcher).forward(request, response);
                verify(response, never()).sendRedirect(anyString());
            }
        }
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void openEdit(boolean found) throws Exception {
        ServiceCategory category = new ServiceCategory();
        try (var services = mockConstruction(ServiceCategoryServiceImpl.class, (service, context) ->
                when(service.getCategoryById(7)).thenReturn(found ? category : null))) {
            new EditServiceCategoryServlet().doGet(request, response);
            verify(services.constructed().get(0)).getCategoryById(7);
            if (found) {
                verify(request).setAttribute("category", category);
                verify(dispatcher).forward(request, response);
            } else {
                verify(response).sendRedirect(LIST);
            }
        }
    }

    @Test void updateCategory() throws Exception {
        when(request.getParameter("status")).thenReturn("INACTIVE");
        try (var services = mockConstruction(ServiceCategoryServiceImpl.class)) {
            new EditServiceCategoryServlet().doPost(request, response);
            var input = ArgumentCaptor.forClass(ServiceCategory.class);
            verify(services.constructed().get(0)).updateCategory(input.capture());
            assertEquals(7, input.getValue().getCategoryId());
            assertEquals("Đồ uống", input.getValue().getCategoryName());
            assertEquals("INACTIVE", input.getValue().getStatus());
            verify(response).sendRedirect(LIST);
        }
    }

    @ParameterizedTest @ValueSource(booleans = {true, false})
    void deleteOutcome(boolean success) throws Exception {
        try (var services = mockConstruction(ServiceCategoryServiceImpl.class, (service, context) ->
                when(service.deleteCategory(7)).thenReturn(success))) {
            new DeleteServiceCategoryServlet().doGet(request, response);
            verify(services.constructed().get(0)).deleteCategory(7);
            verify(session, times(success ? 0 : 1)).setAttribute("error",
                    "Cannot delete category because it is being used.");
            verify(response).sendRedirect(LIST);
        }
    }

    @ParameterizedTest @EmptySource @ValueSource(strings = {"abc", "7.5", "2147483648"})
    void invalidDeleteId(String value) {
        when(request.getParameter("id")).thenReturn(value);
        try (var services = mockConstruction(ServiceCategoryServiceImpl.class)) {
            var controller = new DeleteServiceCategoryServlet();
            assertThrows(NumberFormatException.class, () -> controller.doGet(request, response));
            verifyNoInteractions(services.constructed().get(0));
        }
    }
}
