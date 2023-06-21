function addStudent(even) {
    var formData = new FormData();
    formData.append('current_semester', $('input[id=current_semester]').val());
    formData.append('firstName', $('input[id=firstName]').val());
    formData.append('lastName', $('input[id=lastName]').val());
    formData.append('dob', $('input[id=dob]').val());
    formData.append('address', $('input[id=address]').val());
    formData.append('avatarFile', $('input[id=avatarFile]')[0].files[0]);
    $.ajax({
        url: '/student/addStudent',
        type: 'POST',
        data: formData,
        dataType: 'json',
        contentType: false,
        processData: false,
        success: function (data) {
            if (data !== null && Object.keys(data).length > 0) {
                document.getElementById("current_semester").value = "";
                document.getElementById("firstName").value = "";
                document.getElementById("lastName").value = "";
                document.getElementById("dob").value = "";
                document.getElementById("address").value = "";
                document.getElementById("avatarFile").value = "";
                alert("add successfull");

            } else {
                alert("add not successfull");
            }
        },
        error: function (data) {
            alert(data)
        }
    });


}

var currentNumber = 0;

function searchStudent(targetPageNumber) {
    var selectElement = document.getElementById("mySelect");
    var selectedValue = selectElement.value;
    var searchText = document.getElementById("searchText").value;
    $.ajax({
        url: '/student/searchStudent',
        type: 'POST',
        data: {
            type: selectedValue,
            searchText: searchText,
            targetPageNumber: targetPageNumber,
        },
        success: function (data) {
            if (data !== null && data.content.length > 0) {
                var danhsach = document.getElementById("danhsach");
                var page = document.getElementById("pagination");

                //
                var tbody = "";
                data.content.forEach(function (item) {
                    tbody += "<tr>" +
                        "<td>" + item.id + "</td>" +
                        "<td>" + item.mssv + "</td>" +
                        "<td>" + item.name + "</td>" +
                        "<td>" + item.dob + "</td>" +
                        "<td>" + item.address + "</td>" +
                        "<td>" + item.currentSemester + "</td>" +
                        "<td>" + item.email + "</td>" +
                        "<td><img style='height: 130px' src=\"" + item.avatar + "\" alt=\"studentImage\"></td>" +
                        "<td><button id=\"updateModal\" type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#exampleModalLong\" class='btn btn-primary' onclick=\"showUpdateConfirm('" + item.mssv + "', '" + item.name + "', '" + item.dob + "', '" + item.address + "', '" + item.currentSemester + "', '" + item.email + "', '" + item.avatar + "')\">Update</button>" +
                        " <button class='btn btn-warning' onclick=\"showDeleteConfirm('" + item.mssv + "')\">Delete</button></td>" +
                        "</tr>";
                });

                //
                var pageHtml = "";
                for (let i = 0; i < data.totalPages; i++) {
                    pageHtml += "<li class=\"page-item\"><a class=\"page-link\" onclick=\"searchStudent(" + i + ")\">" + (i + 1) + "</a></li>";
                }
                danhsach.innerHTML = "<tbody id=\"danhsach\" class=''>\n" + tbody + "</tbody>\n";
                page.innerHTML = "  <ul id=\"pagination\" class=\"pagination\">\n" +
                    "    <li class=\"page-item\"><a class=\"page-link\" onclick=\"searchStudent(" + (data.pageable.pageNumber - 1) + ")\">Previous</a></li>\n" +
                    pageHtml +
                    "    <li class=\"page-item\"><a class=\"page-link\" onclick=\"searchStudent(" + (data.pageable.pageNumber + 1) + ")\">Next</a></li>\n" +
                    "  </ul>\n";
            } else {
                // alert("Student not found!!");
            }
        },
        error: function (data) {
            alert("Student not found errol  !!");
        }
    });
}

function showUpdateConfirm(mssv, name, dob, address, currentSemester, email, avatar) {
    document.getElementById("mssvUpdate").value = mssv;
    document.getElementById("current_semester").value = currentSemester;
    document.getElementById("account_mail").value = email;
    document.getElementById("nameUpdate").value = name;
    document.getElementById("dobUpdate").value = dob;
    document.getElementById("addressUpdate").value = address;

    $.ajax({
        url: '/account/findAllEmailNoConnected',
        type: 'Get',
        success: function (data) {
            if (data !== null) {
                var dataList = "";
                for (let i of data) {
                    dataList += "<option value=\"" + i + "\"></option>"
                }
                document.getElementById("options").innerHTML = "<datalist id=\"options\">\n" + dataList + "</datalist>";
            } else {
                alert("load all mail fail");
            }
        },
        error: function (data) {
        }
    });
    $('#UpdateStatic').modal('show');
}

function showDeleteConfirm(mssv) {
    var question = document.getElementById("StudentProperties");
    question.innerHTML = "<p id=\"StudentProperties\">Are you sure you want to delete the student with Mssv: " + mssv + "</p>\n" +
        "<input id=\"mssvDelete\" type=\"hidden\" value=\"" + mssv + "\" />\n";
    $('#staticBackdrop-delete').modal('show');
}

function clickDelete() {
    var mssv = document.getElementById("mssvDelete").value;
    $.ajax({
        url: '/student/deleteStudent',
        type: 'DELETE',
        data: {
            mssv: mssv,
        },
        success: function (data) {
            if (data) {
                alert("Delete Student successful!!");
            } else {
                alert("Delete Student fail!!");
            }
        },
        error: function (data) {
            alert("Delete Student errol!!");
        }
    });
}

function clickUpdate() {
    var formData = new FormData();
    formData.append('mssvUpdate', $('#mssvUpdate').val());
    formData.append('current_semester', $('#current_semester').val());
    formData.append('account_mail', $('#account_mail').val());
    formData.append('nameUpdate', $('#nameUpdate').val());
    formData.append('dobUpdate', $('#dobUpdate').val());
    formData.append('addressUpdate', $('textarea#addressUpdate').val());
    formData.append('avatar', $('#avatar')[0].files[0]);

    $.ajax({
        url: '/student/updateStudent',
        type: 'PUT',
        data: formData,
        dataType: 'json',
        contentType: false,
        processData: false,
        success: function (data) {
            if (data !== null && Object.keys(data).length > 0) {
                alert("update successfull");
            } else {
                alert("update not successfull");
            }
        },
        error: function (data) {
            alert(data)
        }
    });
}

