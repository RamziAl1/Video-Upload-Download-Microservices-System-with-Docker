const form = document.querySelector('#video-form');
const videoDiv = document.querySelector('#video-player');
const videoMessege = document.querySelector('#now-playing');

const queryParams = Object.fromEntries(new URLSearchParams(window.location.search));

fetch('/getvideosdata')
    .then(result => result.json())
    .then(result => {
        const myVids = document.querySelector('#your-videos');
        if(result.length > 0){
            for(let vid of result){
                const li = document.createElement('LI');
                const link = document.createElement('A');
                link.innerText = vid["video_name"];
                link.href = window.location.origin + window.location.pathname + '?video=' + link.innerText; 
                li.appendChild(link);
                myVids.appendChild(li);
            }
        } else {
           myVids.innerHTML = 'No videos found';
        }

    });

if(queryParams.video){
    videoDiv.style.display = 'block';
    videoMessege.innerText = 'Loading video please wait';
    fetch(`/videos/${queryParams.video}`).then(
        setTimeout(() => {
            const videoScreen = document.querySelector('#video-screen');
            videoScreen.src = `./videos/${queryParams.video}.mp4`;
            videoMessege.innerText = 'Now playing ' + queryParams.video;
         }, 5000)
    );
}