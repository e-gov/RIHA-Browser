<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
    <p>Infosüsteemi <a href="${baseUrl}/Infosüsteemid/Vaata/${infoSystem.uuid}">${name}</a> arutelusse "<a href="${baseUrl}/Infosüsteemid/Vaata/${infoSystem.uuid}/Arutelu/${issueId?c}">${issueTitle}</a>" on lisatud uus otsus:</p>
    <p>${decision}<#if comment != ""> (<i>"${comment}"</i>)</#if> - ${author}</p>
    <p>Sisene RIHAsse, et arutelu lähemalt vaadata.</p>
    <p>----</p>
    <p>See on automaatne kiri RIHA rakendusest. Palun ära vasta sellele. Küsimuste korral saada kiri <a href="mailto:help@ria.ee">RIA kasutajatoele</a>.</p>
</body>
</html>
