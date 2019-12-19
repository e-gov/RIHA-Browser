<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body>
    <p>Infosüsteemi <a href="${baseUrl}/Infosüsteemid/Vaata/${infoSystem.shortName}">${infoSystem.fullName}</a> arutelu <a href="${baseUrl}/Infosüsteemid/Vaata/${infoSystem.shortName}/Arutelu/${issue.id?c}">${issue.title}</a> ${commented?then('kommenteeriti ja', '')} ${issue.status?switch('CLOSED', 'suleti', 'OPEN', 'avati', 'staatust muudeti')}. Sisene RIHAsse, et arutelusid täpsemalt vaadata.</p>
    <p>----</p>
    <p>See on automaatne kiri RIHA rakendusest. Palun ära vasta sellele. Küsimuste korral saada kiri <a href="mailto:help@ria.ee">RIA kasutajatoele</a>.</p>
</body>
</html>
