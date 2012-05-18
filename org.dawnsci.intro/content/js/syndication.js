var newcontent = document.createElement('contentProvider');
var newId = 'AAAAAAAAAAAAA';
newcontent.id = newId;
newcontent.setAttribute('pluginId', 'org.dawnsci.intro');
newcontent.setAttribute('class', 'org.dawnsci.intro.content.ASinglePerspectiveContentProvider');
newcontent.appendChild(document.createTextNode('Here is some syndicated content.'));  
var scr = document.getElementById('syndication');
scr.parentNode.insertBefore(newcontent, scr); 