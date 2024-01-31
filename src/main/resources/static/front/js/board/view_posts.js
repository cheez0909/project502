window.addEventListener("DOMContentLoaded", function() {

    const { ajaxLoad } = commonLib;

    let viewPosts = this.localStorage.getItem('viewPosts');
    if(!viewPosts){
        return;
    }

    viewPosts = JSON.parse(viewPosts);

    const qs = viewPosts.map(p => `seq=${p.seq}`).join('&');
    const tpl = this.document.getElementById("posts_tpl").innerHTML;
    const targetEl = this.document.querySelector(".view_posts");

    const domParser = new DOMParser();

    ajaxLoad('GET', `/api/board/view_post?${qs}`, null, 'json')
    .then(res => {
    if(!res.success || !res.data) return;
        const items = res.data;

        for(const item of items){

            let html = tpl;
            html = html.replace(/\[url\]/g,`/board/view/${item.seq}`)
            .replace(/\[subject\]/g, item.subject);

            const dom = domParser.parseFromString(html, "text/html");
            const li = dom.querySelector("li");
            targetEl.appendChild(li);
        }
    })
    .catch(err => console.error(err));
});