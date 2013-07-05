/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



function sendRequest() {

    $.ajax({
        type: 'post',
        url: 'index.xhtml',
        success: function(res) {
            console.log("Request sent to classifier");
            showResult();
        }
    });
}

function showResult() {
    var text = "";
    $.ajax({
        type: 'post',
        url: 'index.xhtml',
        success: function(res) {
            if ($.trim(res) != $.trim($('#showResult').html()))
                $('#showResult').html(res);
                text = res;
        }
    });
    
    return text;
}

