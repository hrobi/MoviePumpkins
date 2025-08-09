import { JSX } from "react";

interface BandParams {
    tag?: keyof JSX.IntrinsicElements
    children: React.ReactNode
}

export default function Band({ children, tag = "div" }: BandParams) {
    
    const CustomTag = tag;
    
    return (<>
        <CustomTag
            className="
                shadow-primary-faint
                border-primary
                border-b-[1px]
                shadow-sm
                bg-white
            "
        >
            {children}
        </CustomTag>   
    </>);
}