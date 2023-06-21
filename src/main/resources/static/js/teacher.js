function hintCourse() {
    return new Promise(function (resolve, reject) {
        var courseList = [];
        $.ajax({
            url: '/course/getAllCourseName',
            type: 'GET',
            success: function (data) {
                if (data !== null) {
                    for (var i of data) {
                        courseList.push(i);
                    }
                    resolve(courseList);
                } else {
                    reject("load all course fail");
                }
            },
            error: function (data) {
                reject(data);
            }
        });
    });
}

function addTeacher() {
    var formData = new FormData();
    formData.append('name', document.getElementById('name').value);
    formData.append('dob', document.getElementById('dob').value);
    formData.append('address', document.getElementById('address').value);
    formData.append('avatarFile', document.getElementById('avatarFile').files[0]);

    var selectedOptions = [];
    var selectElement = document.getElementById('mySelect');
    for (var i = 0; i < selectElement.options.length; i++) {
        if (selectElement.options[i].selected) {
            selectedOptions.push(selectElement.options[i].value);
        }
    }
    formData.append('selectedCourse', selectedOptions);
    $.ajax({
        url: '/teacher/addTeacher',
        type: 'POST',
        data: formData,
        dataType: 'json',
        contentType: false,
        processData: false,
        success: function (data) {
            if (data !== null && Object.keys(data).length > 0) {
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