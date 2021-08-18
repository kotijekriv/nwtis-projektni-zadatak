$(document).ready(function LoadPage() {
    var naslov = $(document).find("title").text();

    switch (naslov) {
        case "Popis mojih aerodroma":
            $("#popisAerodromiPrati").DataTable({});
            promjenaAerodroma();
            break;

         case "Popis korisnika":
            $("#popisKorisnika").DataTable({});
            break;
    }
});

function promjenaAerodroma() {
    $("#aerodrom").on("change", function () {
        var a = $("option:selected", this).attr("data-naziv");
        $("#odabraniAerodrom").attr("value", a);
    });
};
