function isAlphaKey(evt) {
    evt = (evt) ? evt : event;
    var charCode = (evt.charCode) ? evt.charCode : ((evt.keyCode) ? evt.keyCode :
            ((evt.which) ? evt.which : 0));
    if (charCode > 33 && (charCode < 65 || charCode > 90) &&
            (charCode < 97 || charCode > 122)) {
//        alert("Masukkan hanya huruf");
        return false;
    }
    return true;
}