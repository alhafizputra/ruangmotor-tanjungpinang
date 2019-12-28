function converJ2K(src) {

    // Modify this to test new images

    var filename = '/Biotest/jo/B_Face';
    var extension = 'j2k';
    var src = filename + '.' + extension;
    console.log("line setelah ");
    var xhr = new XMLHttpRequest();
    xhr.open('GET', src);

    xhr.mozResponseType = xhr.responseType = 'arraybuffer';
    xhr.onreadystatechange = function (e) {

        if (xhr.readyState !== 4)
            return;
        if (xhr.status !== 200 && xhr.status !== 0)
            throw "Error in XHR response (status = " + xhr.status + ")";
        var buffer = (xhr.mozResponseArrayBuffer || xhr.mozResponse ||
                xhr.responseArrayBuffer || xhr.response);
        var bytes = new Uint8Array(buffer);

        var t0 = new Date().getTime();
        var rgbImage = openjpeg(bytes, 'j2k');
        console.log('---> openjpeg() total time: ', ((new Date().getTime()) - t0) + 'ms');
        alert("Atas ! " + src);
        var canvas = document.getElementById('canvas');
        alert("Bawah ! " + rgbImage.width + "ava" + rgbImage.height);
        canvas.width = 354;
        canvas.height = 471;

        var pixelsPerChannel = 354 * 471;
        var ctx = canvas.getContext('2d');
        var rgbaImage = ctx.createImageData(354, 471);
        var i = 0, j = 0;
        while (i < rgbaImage.data.length && j < pixelsPerChannel) {
            rgbaImage.data[i] = rgbImage.data[j]; // R
            rgbaImage.data[i + 1] = rgbImage.data[j + pixelsPerChannel]; // G
            rgbaImage.data[i + 2] = rgbImage.data[j + 2 * pixelsPerChannel]; // B
            rgbaImage.data[i + 3] = 255; // A
            // Next pixel
            i += 4;
            j += 1;
        }
        ctx.putImageData(rgbaImage, 0, 0);
    }
    xhr.send(null);
}