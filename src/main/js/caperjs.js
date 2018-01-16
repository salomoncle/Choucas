function getRandos(){
    var randos = document.querySelectorAll('div.divItemRando')
    var listRando = []
    for (var i = 0; i < randos.length; i++) {
        var rando = {}
        var infos = randos[i].querySelector('div.contentEncartRando2')
        rando['description'] = randos[i].querySelector('p').innerHTML.replace('<strong>','').replace('</strong>','').replace('<br>','')
        rando['titre'] = randos[i].querySelector('a').innerHTML
        rando['url'] = randos[i].querySelector('a').getAttribute('href')
        randos[i].querySelector('a')
        listRando.push(rando)
    }
    return listRando
}





function getDetails(){
    var details = document.querySelectorAll('div.col50')
    var rez = {}
    var detail
    detail = details[0].innerHTML
    detail += details[1].innerHTML

    var duree = (detail.split("<strong>Durée moyenne: </strong>")[1].split("<br")[0]).split('<span')[0]
    var distance = (detail.split("<strong>Distance : </strong>")[1].split("<br")[0]).split('km')[0]
    var deniveleP = (detail.split("<strong>Dénivelé positif : </strong>")[1].split("<br")[0]).split('m')[0]
    var deniveleM = (detail.split("<strong>Dénivelé négatif : </strong>")[1].split("<br")[0]).split('m')[0]
    var pointHaut = (detail.split("<strong>Point haut : </strong>")[1].split("<br")[0]).split('m')[0]
    var pointBas = (detail.split("<strong>Point bas : </strong>")[1].split("<br")[0]).split('m')[0]
    var difficulte = (detail.split("<strong>Difficulté : </strong>")[1].split("<br")[0]).split('<br')[0]
    var regions = (detail.split("<strong>Régions : </strong>")[1].split("<br")[0]).split(',')
    for (var i = 0; i < regions.length; i++) {
        regions[i] = regions[i].split('.html">')[1].split('</a>')[0]
    }
    var commune = (detail.split(" <strong>Commune : </strong>")[1].split(")<")[0]).split('.html">')[1].replace('(','').replace('</a>', '')
    var depart = ((detail.split("<strong>Départ : </strong>")[1].split("<br")[0]).split('</a><br/>')[0]).split('rel="nofollow">')[1].split('</a>')[0]
    var retourPointBas = (detail.split("<strong>Retour point de départ : </strong>")[1].split("<br")[0]).split('<br')[0]


    rez['duree'] = duree ? duree : "";
    rez['distance'] = distance ? distance : "";
    rez['deniveleP'] = deniveleP ? deniveleP : "";
    rez['deniveleN'] = deniveleM ? deniveleM : "";
    rez['pointHaut'] = pointHaut ? pointHaut : "";
    rez['pointBas'] = pointBas ? pointBas: "";
    rez['difficulte'] = difficulte ? difficulte : "";
    rez['regions'] = regions ? regions : [];
    rez['commune'] = commune ? commune: "";
    rez['depart'] = depart ? depart : "";
    rez['retourPointBas'] = retourPointBas ? retourPointBas : "";

    return  rez

}

var Randos = []
var Data = []
var i=-1
var currentRando = 0







var casper = require('casper').create();
var url = 'https://www.visorando.com/index.php?component=rando&task=searchCircuit&idEG=0';


casper.start(url);


casper.then(function() {
    if (this.exists('div.rando-results')){
        //this.echo(this.getCurrentUrl())

        var listRando = this.evaluate(getRandos)
        Randos = listRando
        /*
        for (var i = 0; i < listRando.length; i++) {
            this.echo(listRando[i]['description'])
        }*/


    }
    else
        this.echo("ERREUR sur le site vision rando")
});

casper.then(function() {
    var INPUT_NUMBER = casper.cli.get("number");
    currentRando = INPUT_NUMBER
    //this.echo(Randos[i]['url'])// change the link being opened (has to be here specifically)
    this.thenOpen((Randos[currentRando]['url']), function() {

        var rez = this.evaluate(getDetails)

        //this.echo(JSON.stringify(rez))

        rez['titre'] = Randos[currentRando]['titre'] ? Randos[currentRando]['titre'] : ""
        rez['description'] = Randos[currentRando]['description'] ? Randos[currentRando]['description'] : ""
        rez['url'] = Randos[currentRando]['url'] ? Randos[currentRando]['url'] : ""
        rez['source'] = "https://www.visorando.com"
        this.echo(JSON.stringify(rez))


        /*
        this.echo("Randonnée : "+rez['titre'])
        this.echo("Url : "+rez['url'])
        this.echo("Description : "+rez['description'])


        this.echo("durée : "+rez['duree'])
        this.echo("distance parcourue : "+rez['distance'])
        this.echo("dénivelé positif : "+rez['deniveleP'])
        this.echo("dénivelé négatif : "+rez['deniveleM'])
        this.echo("Point haut : "+rez['pointHaut'])
        this.echo("Point bas : "+rez['pointBas'])
        this.echo("Difficulté : "+rez['difficulte'])
        for (var i = 0; i < rez['regions'].length; i++) {
            this.echo("Région "+i+" : "+rez['regions'][i])
        }
        this.echo("Commune : "+rez['commune'])
        this.echo("Position du départ : "+rez['depart'])
        this.echo("Retour en bas ? "+rez['retourPointBas'])
        this.echo('')
        this.echo('')
        this.echo('')
        */

    });


//this.echo(JSON.stringify(Data))

});


casper.run();
