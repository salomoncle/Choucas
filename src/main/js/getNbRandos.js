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

var Randos = []

var casper = require('casper').create();
var url = 'https://www.visorando.com/index.php?component=rando&task=searchCircuit&idEG=0';
casper.start(url);


casper.then(function() {
    if (this.exists('div.rando-results')){
        //this.echo(this.getCurrentUrl())

        var listRando = this.evaluate(getRandos)
        Randos = listRando
        this.echo(Randos.length)
        /*
        for (var i = 0; i < listRando.length; i++) {
            this.echo(listRando[i]['description'])
        }*/


    }
    else
        this.echo("ERREUR sur le site vision rando")
});

casper.run();