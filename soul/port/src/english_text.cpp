#include "english_text.h"
#include <stdexcept>

namespace soul {
void applyEnglishFrontendText(std::vector<std::u16string>& s) {
    if(s.size()<=42)throw std::invalid_argument("Incomplete common text for English UI");
    s[10]=u"PRESS ANY KEY";
    s[40]=u"Darkness has always cast an ominous shadow over human history. No legend was more terrifying than that of the vampires.^^"
        u"When Kale was a child, vampires attacked his family and killed both his parents. He devoted his life to hunting vampires, learning how to fight them and survive.^^"
        u"After three years of searching, Kale finally finds a lead in the town of Ominos. The townspeople suffer constant attacks by the undead and werewolves. "
        u"A powerful vampire must be ruling them. Gripping his fire sword, Kale enters the town to take his revenge...";
    s[41]=u"CHAPTER"; s[42]=u"THE CHALLENGE";
    // Translate headings in place, keeping all credited people in original order.
    auto& credits=s[28];
    const auto replace=[&](const std::u16string& from,const std::u16string& to){
        std::size_t pos=0;
        while((pos=credits.find(from,pos))!=std::u16string::npos){credits.replace(pos,from.size(),to);pos+=to.size();}
    };
    replace(u"Verze:",u"Version:"); replace(u"Info a podpora:",u"Information and support:");
    const std::u16string headings[]{u"Executive producers",u"Producers",u"Game design managers",u"Creative lead",u"Game design",
        u"Art managers",u"Lead artist",u"Art",u"Programming managers",u"Lead programmer",u"Programming",u"Audio lead",u"Audio",
        u"Quality assurance managers",u"Quality assurance leads",u"Quality assurance",u"Localization manager",u"Localization",
        u"Studio manager",u"Special thanks"};
    std::size_t pos=0,index=0;
    while((pos=credits.find(u'{',pos))!=std::u16string::npos){
        const auto end=credits.find(u'}',pos);
        if(end==std::u16string::npos || index>=20)throw std::invalid_argument("Unexpected credit headings");
        credits.replace(pos+1,end-pos-1,headings[index]);pos+=headings[index++].size()+2;
    }
    if(index!=20)throw std::invalid_argument("Incomplete credit headings");
}
}
