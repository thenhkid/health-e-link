<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="login-container">
    <div class="login clearfix">
        <header class="login-header" role="banner">
            <div class="login-header-content">
                <a href="<c:url value='/' />" title="Return to home page.">
                    <span class="logo" alt="{Company Name Logo}"></span>
                </a>
            </div>
        </header>
        <c:if test="${not empty error}">
            <div class="alert alert-danger center-text" role="alert">
                Your login attempt was unsuccssful.<br />
                - ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} -
            </div>
        </c:if>
        <form role="form" id="form-admin-login" name='f' action="<c:url value='j_spring_security_check' />" method='POST'>
            <fieldset name="login-fields" form="form-admin-login" class="basic-clearfix">
                <div class="form-group ${not empty error ? 'has-error' : '' }">
                    <label class="control-label" for="username">Username</label>
                    <input id="username" name='j_username' class="form-control" type="text" value="admin" />
                </div>
                <div class="form-group ${not empty error ? 'has-error' : '' }">
                    <label class="control-label" for="password">Password</label>
                    <input id="password" name='j_password' class="form-control" type="password" value="!admin!" />
                </div>
                <input type="submit" value="Login" class="btn btn-primary pull-right" role="button"/>
                <!--<label for="remember-me" class="pull-left"><input id="j_remember" name="_spring_security_remember_me" type="checkbox" value="1">&nbsp;Remember Me</label>-->
            </fieldset>
        </form>
    </div>
    <p class="login-note"><a href="">Forgot Password?</a></p>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        document.f.j_username.focus();
    });
</script>