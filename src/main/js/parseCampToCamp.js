
function getJson(){
    return document.querySelector('body').innerText

}



var casper = require('casper').create();
var url = casper.cli.get("url");
casper.start(url);

casper.then(function () {
    if(this.exists('body')) {
        var json = JSON.parse(casper.getPageContent())
        var rez={}
        rez['titre'] = json.locales[0].title
        rez['description'] = json.locales[0].description
        rez['url'] = url
        rez['source'] = "https://www.camptocamp.org/"

        rez['regions']=""
        Object.keys(json.areas[1].locales).forEach(function (key) {
            if (json.areas[1].locales[key].lang == "fr")
                rez['regions']= [json.areas[1].locales[key].title]
        })

        rez['commune']=""
        if (json.areas[2])
            Object.keys(json.areas[2].locales).forEach(function (key) {
                if (json.areas[2].locales[key].lang == "fr")
                    rez['commune']=json.areas[2].locales[key].title
            })



        rez['deniveleP'] = json.height_diff_up
        rez['deniveleN'] = json.height_diff_down
        rez['distance'] = json.length_total
        rez['duree'] = json.locales[0].timing
        rez['pointHaut'] = json.elevation_max
        rez['pointBas'] = json.elevation_min
        rez['retourPointBas'] = ""
        rez['depart'] = json.geometry.geom.toString()
        rez['difficulte'] = ""
        if (json.hiking_rating) rez['difficulte'] = json.hiking_rating

        this.echo(JSON.stringify(rez))






    }



    
})
casper.run()