const img = document.getElementById('frame') as HTMLImageElement;
const fpsSpan = document.getElementById('fps')!;
const input = document.getElementById('file') as HTMLInputElement;

let last = performance.now();
let frames = 0;

function updateFPS() {
    frames++;
    const now = performance.now();
    if (now - last >= 1000) {
        fpsSpan.textContent = (frames).toString();
        frames = 0;
        last = now;
    }
    requestAnimationFrame(updateFPS);
}

input.addEventListener('change', (e) => {
    const f = input.files?.[0];
    if (!f) return;
    const reader = new FileReader();
    reader.onload = () => {
        img.src = reader.result as string;
    };
    reader.readAsDataURL(f);
});

updateFPS();
