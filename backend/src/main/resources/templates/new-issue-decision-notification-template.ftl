<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
    <p>Infosüsteemi <a href="${baseUrl}/Infosüsteemid/Vaata/${shortName}">${name}</a> arutelusse "<a href="${baseUrl}/Infosüsteemid/Vaata/${shortName}/Arutelu/${issueId?c}">${issueTitle}</a>" on lisatud uus otsus:</p>
    <p>${decision}<#if comment != ""> (<i>"${comment}"</i>)</#if> - ${author}</p>
    <p>Sisene RIHAsse, et arutelu lähemalt vaadata.</p>
    <p>----</p>
    <p>Tegemist on RIHA automaatkirjaga. Vabandame, kui saite selle kirja ekslikult. Sel juhul andke sellest palun teada <a href="mailto:help@ria.ee">RIA kasutajatoele</a>.</p>
</body>
</html>