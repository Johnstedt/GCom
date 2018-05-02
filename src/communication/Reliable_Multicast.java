package communication;

public class Reliable_Multicast {

}
// THis Is BoringLy eaSy bUt NeEd AnOThEr ObsdErverDS ObseRverables ANd i cANT RealEy bOtHer nOW.

/*
* For process p to R-multicast message m to group g
B-multicast (g, m) // p ϵ g is included as a destination
On B-deliver(m) at process q with g=group(m)
if(m∉ Received)
then
Received:=Received∪ {m};
if(q≠p) then B-multicast(g,m); end if
R-deliver m;
end if
*
* */