<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<!DOCTYPE html>

<html lang="vi">

<head>

    <meta charset="UTF-8">

    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <meta name="description"
          content="Movie Ticket Management System">

    <meta name="author"
          content="Movie Ticket Management Team">

    <meta name="keywords"
          content="Movie, Cinema, Ticket, Booking">

    <!-- Favicon -->
    <link rel="icon"
          type="image/x-icon"
          href="${pageContext.request.contextPath}/assets/images/favicon.ico">

    <!-- Dynamic Title -->
    <title>
        ${empty pageTitle ? 'Movie Ticket Management' : pageTitle}
    </title>

    <!-- Bootstrap CSS -->
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet">

    <!-- Bootstrap Icons -->
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
        rel="stylesheet">

    <!-- Google Fonts (Quicksand - phong cách mini/cute) -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Quicksand:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Project CSS -->
    <link
        rel="stylesheet"
        href="${pageContext.request.contextPath}/assets/css/style.css">

    <!-- Bootstrap JS -->
    <script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
    </script>

</head>