<html>
<head>
<title>video submission page</title>
</head>
<body>
<p>${status}</p>
<form action="/submit-video" method="post" enctype="multipart/form-data">
<fieldset>
                <legend>Upload a video</legend>
                <label for="file">Video upload</label>
                <input id="file" name="file" type="file" enctype="multipart/form-data">
                <label for="name">Video Name</label>
                <input id="name" name="video-name" type="text">
                <button type="submit">upload</button>
            </fieldset>
</form>

</body>
</html>