<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:useBean id="date" class="java.util.Date" />

<div class="module email-list-signup">
    <div class="container">
        <div class="alert alert-danger error" style="display: none;">
            <strong>Error!</strong> <span id="errMsg">The email address entered is invalid!</span>
        </div>
        <div class="alert alert-success success" style="display: none;">
            <strong>Success!</strong> <span id="successMsg">The email address hass been registered.</span>
        </div>
        <form class="form-inline">
            <h4 class="pull-left">Join Our Email List</h4>
            <div class="form-group">
                <label class="sr-only">Join our email list</label>
                <input class="form-control" type="text" id="emailAddress" placeholder="Email Address">
            </div>
            <input type="button" class="btn btn-secondary" value="Submit" id="saveEmail" />
            <div class="radio">
                <label><input type="radio" value="1" name="unsubscribe" id="unsubscribe" /> Unsubscribe</label>
            </div>
        </form>
    </div>
</div>
<footer class="footer">
    <div class="container">
        <nav>
            <ul class="nav-inline">
                <li><a href="<c:url value='/'/>" title="Home">Home</a></li>
                <li><a href="<c:url value='/about'/>" title="About">About</a></li>
                <li><a href="<c:url value='/contact'/>" title="Contact">Contact</a></li>
                <li><a href="<c:url value='/privacy'/>" title="Contact">Privacy</a></li>
                <c:if test="${not empty pageContext.request.userPrincipal.name}"><li><a href="<c:url value='/profile'/>" title="My Account">My Account</a></li></c:if>
                </ul>
            </nav>

            <p class="vcard">
                <span class="fn">BOWlink Technologies Inc.</span> |
                <span class="adr"><span class="post-office-box">P.O. Box 275</span>,
                    <span class="region">Auburn, MA</span>
                    <span class="postal-code">01501</span></span> |
                Phone: <span class="tel">(508) 721-1977</span> |
            </p>
            <p>
                Copyright <fmt:formatDate value="${date}" pattern="yyyy" /> Massachusetts Department of Public Health All rights reserved</p>
        </p>
    </div>
</footer>

<script>

    require(['./main'], function() {
        require(['jquery'], function($) {

            $("input:text,form").attr("autocomplete", "off");

            //This function will open up the modal displaying the note for the
            //selected bp entry
            $(document).on('click', '#saveEmail', function() {

                $('.error').hide();
                $('.success').hide();

                var validEmail = validateEmail($('#emailAddress').val());
                var unsubscribe = false;
                
                if($('#unsubscribe').prop('checked') == true) {
                    unsubscribe = true;
                }
                

                if (validEmail == true) {
                    $.ajax({
                        url: 'emailSignUp.do',
                        data: {'emailAddress': $('#emailAddress').val(), 'unsubscribe': unsubscribe},
                        type: "POST",
                        success: function(data) {
                            if (data == 1) {
                                $('.success').show();
                                $('.error').hide();
                                $('#emailAddress').val('');
                                $('.alert').delay(2000).fadeOut(1000);
                            }
                            else if(data == 2) {
                                $('#errMsg').html("The email address is already registered.");
                                $('.error').show();
                                $('.alert').delay(2000).fadeOut(1000);
                            }
                            else {
                                $('#successMsg').html("The email address has been removed from our list.");
                                $('.success').show();
                                $('.alert').delay(2000).fadeOut(1000);
                            }
                        }
                    });
                }
                else {
                    $('.error').show();
                    $('.alert').delay(2000).fadeOut(1000);
                }
            });


        });
    });

    function validateEmail($email) {
        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
        if ($email == '' || !emailReg.test($email)) {
            return false;
        }
        else {
            return true;
        }

    }
</script>
