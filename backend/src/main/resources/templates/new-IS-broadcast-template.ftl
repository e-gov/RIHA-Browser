<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body bgcolor="#FGFGFG" style="background:#fgfgfg;">
<table bgcolor="#FGFGFG" align="center" border="0" cellpadding="0" cellspacing="0" style="margin:0 auto;width:100%">
    <tbody>
    <tr>
        <td align="center" bgcolor="#FGFGFG" style="background:#fgfgfg;">
            <table align="center" style="max-width:630px; width:100%; margin-top: 20px;" border="0" cellspacing="0"
                   cellpadding="0">
                <tbody>
                <tr>
                    <td align="center">
                        <table align="center" border="0" cellpadding="0" cellspacing="0"
                               style="margin:0 auto;width:100%">
                            <tbody>
                            <tr bgcolor="#ffffff">
                                <td style="border-top:2px solid #023cc1; border-right:2px solid #023cc1;border-left:2px solid #023cc1">
                                    <img class="card-img" src="${baseUrl}/assets/images/email-header.gif" alt=""
                                         style="display:block; height:auto;" height="100%" width="auto">
                                </td>
                            </tr>
                            <tr bgcolor=#FFFFFF>
                                <td style="padding-top:30px;padding-right:30px;padding-left:30px;padding-bottom:1px; border-bottom:2px solid #023cc1; border-right:2px solid #023cc1;border-left:2px solid #023cc1">
                                    <table border="0" dir="ltr" cellpadding="0" cellspacing="0" width="100%">
                                        <tbody>
                                        <tr>
                                            <td dir="ltr" align="center"
                                                style="text-align:center;padding-bottom:15px;font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:400;font-size:22px;line-height:32px;color:#023cc1">
                                                Uued infosüsteemid RIHAs
                                            </td>
                                        </tr>
                                        <tr>
                                            <td dir="ltr" align="left"
                                                style="text-align:center;padding-bottom:30px;font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:normal;font-size:16px;line-height:24px;color:#808080">
                                                RIHAsse on sisestatud uusi infosüsteeme, millele on võimalik tagasisidet
                                                anda. Viimase 24 tunni jooksul on lisandunud:
                                            </td>
                                        </tr>
                                        <#list infosystems as infoSystem>
                                            <tr>
                                                <td dir="ltr" align="left"
                                                    style="text-align:center;font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:normal;font-size:16px;line-height:24px">
                                                    <a href="${baseUrl}/Systems/Vaata/${infoSystem.shortName}"
                                                       style="color:#555555; text-decoration: none">${infoSystem.fullName}</a>
                                                </td>
                                            </tr>
                                        </#list>
                                        <td bgcolor="#ffffff" align="center"
                                            style="padding-top:18px;padding-bottom:0px">
                                            <table width="80%" border="0" cellpadding="0" cellspacing="0"
                                                   class="call-to-action">
                                                <tbody>
                                                <tr>
                                                    <td align="center" dir="ltr" style="padding-bottom: 40px;">
                                                        <center><a href="${baseUrl}"
                                                                   style="background:#ffffff;border:2px solid #023cc1;border-radius:10px;color:#023cc1;display:block;font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:400; font-size: 16px; padding:14px 30px;text-decoration:none"
                                                                   class="reg_button" target="_blank">SISENE RIHASSE</a>
                                                        </center>
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:40px;padding-right:100px;padding-bottom:0px;padding-left:100px"
                                    width="80%">
                                    <table align="right" border="0" cellpadding="0" cellspacing="0" width="100%">
                                        <tbody>
                                        <tr>
                                            <td height="1" bgcolor="#fgfgfg">
                                                <div style="height:1px;width:100%;margin:0 auto;background:#dddddd">
                                                    &nbsp;
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td dir="ltr" bgcolor="#fgfgfg" align="center"
                                                style="padding:30px;font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:normal;font-size:12px;line-height:16px;color:#9fb1c1">
                                                <p>See sõnum on saadetud sulle RIHA tiimi poolt. Kui sa ei soovi enam
                                                    uudiskirju saada, anna sellest teada <a
                                                            href="mailto:help@ria.ee?subject=Loobun RIHA uudiskirjast"
                                                            style="font-family:Roboto,Helvetica Neue,Helvetica,Arial,sans-serif;font-weight:normal;font-size:12px;line-height:16px;color:#9fb1c1">
                                                        RIA kasutajatoele</a>.</p>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
    </tr>
    </tbody>
</table>
</body>
</html>